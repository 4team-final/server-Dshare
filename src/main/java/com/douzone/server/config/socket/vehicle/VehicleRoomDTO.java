package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.socket.Calendar;
import com.douzone.server.exception.WebsocketIOException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.Principal;
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

	public void handlerActions(WebSocketSession session, VehicleSocketDTO vehicleSocketDTO, VehicleSocketService service) {
		if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.ENTER)) {
			String empNo = Optional.ofNullable(session.getPrincipal())
					.map(Principal::getName)
					.orElseThrow(() -> new UsernameNotFoundException("websocket"));
			for (WebSocketSession webSocketSession : sessions) {
				String compareEmpNo = webSocketSession.getPrincipal().getName();
				if (compareEmpNo.equals(empNo)) {
					sendMessage(session, FAIL_DOUBLE_ACCESS_SOCKET_CONNECT, service);
					return;
				}
			}
			sessions.add(session);
			autoDisconnect(session, service);
			sendMessage(vehicleSocketDTO.getEmpNo() + VehicleSocketDTO.MessageType.ENTER, service);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.TALK)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getTime(), vehicleSocketDTO.getEmpNo());
			

			sendMessage(vehicleSocketDTO, service);
			sessions.remove(session);
			close(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.DUAL)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getMessage(), vehicleSocketDTO.getTime()[0], vehicleSocketDTO.getTime()[1], vehicleSocketDTO.getEmpNo());
			sendMessage(vehicleSocketDTO, service);
			sessions.remove(session);
			close(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.QUIT)) {
			sendMessage(session, SUCCESS_DISCONNECT_VEHICLE_SOCKET, service);
			sessions.remove(session);
			close(session);
		} else {
			if (sessions.contains(session)) {
				sendMessage(session, FAIL_ACCESS_SOCKET_CONNECT, service);
				sessions.remove(session);
				close(session);
				return;
			}
			sendMessage(session, FAIL_ACCESS_SOCKET_TYPE, service);
		}
	}

	private <T> void sendMessage(T message, VehicleSocketService service) {
		sessions.parallelStream()
				.forEach(session -> service.sendMessage(session, message));
	}

	private <T> void sendMessage(WebSocketSession s, T message, VehicleSocketService service) {
		service.sendMessage(s, message);
	}

	public VehicleRoomDTO of(Calendar calendar) {
		return VehicleRoomDTO.builder()
				.uid(calendar.getUid())
				.build();
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

	private void close(WebSocketSession session) {
		try {
			session.close();
		} catch (IOException e) {
			throw new WebsocketIOException(SOCKET_NOT_CLOSE_ERROR);
		}
	}

}