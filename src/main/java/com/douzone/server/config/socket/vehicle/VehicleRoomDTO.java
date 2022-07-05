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
//			Optional.ofNullable(session.getPrincipal())
//					.map(Principal::getName)
//					.map(res -> sessions.stream()
//							.filter(v -> v.getPrincipal() != null)
//							.map(v -> v.getPrincipal().getName())
//							.filter(v -> v.equals(res))
//							.peek(v -> sendMessage(session, FAIL_DOUBLE_ACCESS_SOCKET_CONNECT, service)))
//					.orElseThrow(() -> new UsernameNotFoundException("websocket session is empty"));
//			String empNo = Objects.requireNonNull(session.getPrincipal()).getName();
//			for (WebSocketSession webSocketSession : sessions) {
//				String compareEmpNo = Objects.requireNonNull(webSocketSession.getPrincipal()).getName();
//				if (compareEmpNo.equals(empNo)) {
//					sendMessage(session, FAIL_DOUBLE_ACCESS_SOCKET_CONNECT, service);
//					return;
//				}
//			}
			sessions.add(session);
			List<VehicleSocketResDTO> list = service.selectTime(vehicleSocketDTO.getUid(), vehicleSocketDTO.getVehicleId());
			SocketResDTO socketResDTO = SocketResDTO.builder()
					.results(list)
					.message(vehicleSocketDTO.getEmpNo() + " 사번의 사원이 " + vehicleSocketDTO.getUid() + " 날짜의 " + vehicleSocketDTO.getVehicleId() + " 번 차량을 구경중입니다.")
					.build();

			sendMessage(socketResDTO, service);

			autoDisconnect(session, service);
			sendMessage(vehicleSocketDTO.getEmpNo() + VehicleSocketDTO.MessageType.ENTER, service);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.TALK)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getTime(), vehicleSocketDTO.getEmpNo());
			List<VehicleSocketResDTO> list = service.selectTime(vehicleSocketDTO.getUid(), vehicleSocketDTO.getVehicleId());
			SocketResDTO socketResDTO = SocketResDTO.builder().results(list).message(vehicleSocketDTO.getEmpNo() + " 사번의 사원이 " + vehicleSocketDTO.getUid() + " 날짜의 " + vehicleSocketDTO.getVehicleId() + " 번 차량을 선점하였습니다.").build();
			sendMessage(socketResDTO, service);
//			remove(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.DUAL)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getMessage(), vehicleSocketDTO.getTime()[0], vehicleSocketDTO.getTime()[1], vehicleSocketDTO.getEmpNo());
			List<VehicleSocketResDTO> list = service.selectTime(vehicleSocketDTO.getUid(), vehicleSocketDTO.getVehicleId());
			sendMessage(list, service);
			remove(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.QUIT)) {
			VehicleSocketResDTO vehicleSocketResDTO = VehicleSocketResDTO.builder()
					.uid(vehicleSocketDTO.getUid())
					.empNo(vehicleSocketDTO.getEmpNo())
					.build();
			sendMessage(vehicleSocketResDTO, service);
			sendMessage(session, SUCCESS_DISCONNECT_VEHICLE_SOCKET, service);
			remove(session);
		} else {
			if (sessions.contains(session)) {
				sendMessage(session, FAIL_ACCESS_SOCKET_CONNECT, service);
				remove(session);
				return;
			}
			sendMessage(session, FAIL_ACCESS_SOCKET_TYPE, service);
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
			if (sessions.size() < 2) return;
			if (session.isOpen()) {
				session.close();
			}
		} catch (IOException e) {
			throw new WebsocketIOException(SOCKET_NOT_CLOSE_ERROR);
		}
	}
}