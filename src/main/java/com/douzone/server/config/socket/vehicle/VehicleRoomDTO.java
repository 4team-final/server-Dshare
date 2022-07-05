package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.socket.Calendar;
import com.douzone.server.config.socket.SocketResDTO;
import com.douzone.server.exception.WebsocketIOException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

import static com.douzone.server.config.utils.Msg.*;
import static com.douzone.server.exception.ErrorCode.SOCKET_NOT_CLOSE_ERROR;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class VehicleRoomDTO {
	private String uid;
	@JsonIgnore
	private final Set<WebSocketSession> sessions = new HashSet<>();
	private int peopleNum;
	private List<String> empNoList;

	@Builder
	public VehicleRoomDTO(String uid) {
		this.uid = uid;
	}

	@Synchronized
	public void handlerActions(WebSocketSession session, VehicleSocketDTO vehicleSocketDTO, VehicleSocketService service) {
		if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.ENTER)) {
			sessions.add(session);
			List<VehicleSocketResDTO> list = service.selectTime(vehicleSocketDTO.getUid(), vehicleSocketDTO.getVehicleId());
			SocketResDTO socketResDTO = SocketResDTO.builder()
					.results(list)
					.message(vehicleSocketDTO.getEmpNo() + " 사번의 사원이 " + vehicleSocketDTO.getUid() + " 날짜의 " + vehicleSocketDTO.getVehicleId() + " 번 차량을 구경중입니다.")
					.build();

			sendMessage(socketResDTO, service);
			autoDisconnect(session, service);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.TALK)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getTime(), vehicleSocketDTO.getEmpNo());
			List<VehicleSocketResDTO> list = service.selectTime(vehicleSocketDTO.getUid(), vehicleSocketDTO.getVehicleId());
			SocketResDTO socketResDTO = SocketResDTO.builder()
					.results(list)
					.message(vehicleSocketDTO.getEmpNo() + " 사번의 사원이 " + vehicleSocketDTO.getUid() + " 날짜의 " + vehicleSocketDTO.getVehicleId() + " 번 차량을 선점하였습니다.")
					.build();
			sendMessage(socketResDTO, service);
//			remove(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.DUAL)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getMessage(), vehicleSocketDTO.getTime()[0], vehicleSocketDTO.getTime()[1], vehicleSocketDTO.getEmpNo());
			List<VehicleSocketResDTO> list = service.selectTime(vehicleSocketDTO.getUid(), vehicleSocketDTO.getVehicleId());
			SocketResDTO socketResDTO = SocketResDTO.builder()
					.results(list)
					.message(vehicleSocketDTO.getEmpNo() + " 사번의 사원이 " + vehicleSocketDTO.getUid() + " 날짜의 " + vehicleSocketDTO.getVehicleId() + " 번 차량을 선점하였습니다.")
					.build();
			sendMessage(socketResDTO, service);
			remove(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.QUIT)) {
			SocketResDTO socketResDTO = SocketResDTO.builder()
					.message(vehicleSocketDTO.getEmpNo() + " 사번의 사원이 " + vehicleSocketDTO.getUid() + "방에서 접속을 종료하였습니다.")
					.build();
			sendMessage(socketResDTO, service);
			sendMessage(session,
					SocketResDTO.builder()
							.message(SUCCESS_DISCONNECT_VEHICLE_SOCKET)
							.build(),
					service);
			remove(session);
		} else {
			if (sessions.contains(session)) {
				sendMessage(session,
						SocketResDTO.builder()
								.message(FAIL_ACCESS_SOCKET_CONNECT)
								.build(),
						service);
				remove(session);
				return;
			}
			sendMessage(session,
					SocketResDTO.builder()
							.message(FAIL_ACCESS_SOCKET_TYPE)
							.build(),
					service);
		}
	}

	private <T> void sendMessage(T message, VehicleSocketService service) {
		sessions.parallelStream()
				.forEach(session -> {
					if (session.isOpen()) {
						service.sendMessage(session, message);
					}
				});
	}

	private <T> void sendMessage(WebSocketSession s, T message, VehicleSocketService service) {
		service.sendMessage(s, message);
	}

	public VehicleRoomDTO of(Calendar calendar) {
		return VehicleRoomDTO.builder()
				.uid(calendar.getUid())
				.build();
	}

	public void remove(WebSocketSession session) {
		sessions.remove(session);
		close(session);
	}

	public void autoDisconnect(WebSocketSession session, VehicleSocketService service) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int i = 180;

			@Override
			public void run() {
				i--;
				if (i < 0) {
					sendMessage(session, TIMEOUT_CONNECT_VEHICLE_SOCKET, service);
					sessions.remove(session);
					timer.cancel();
					close(session);
				}
			}
		}, 0, 180000);
	}

	@Synchronized
	private void close(WebSocketSession session) {
		try {
			if (session.isOpen()) {
				session.close();
			}
		} catch (IOException e) {
			throw new WebsocketIOException(SOCKET_NOT_CLOSE_ERROR);
		}
	}
}