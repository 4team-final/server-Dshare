package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.socket.Calendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

import static com.douzone.server.config.utils.Msg.*;

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
			sessions.add(session);
			autoDisconnect(session, service);
			sendMessage(vehicleSocketDTO.getEmpNo() + VehicleSocketDTO.MessageType.ENTER, service);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.TALK)) {
			service.updateIsSeat(vehicleSocketDTO.getVehicleId(), vehicleSocketDTO.getUid(), vehicleSocketDTO.getTime(), vehicleSocketDTO.getEmpNo());
			sendMessage(vehicleSocketDTO, service);
			sessions.remove(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.DUAL)) {

			sendMessage(vehicleSocketDTO, service);
			sessions.remove(session);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.QUIT)) {
			sendMessage(session, SUCCESS_DISCONNECT_VEHICLE_SOCKET, service);
			sessions.remove(session);
		} else {
			if (sessions.contains(session)) {
				sendMessage(session, FAIL_ACCESS_SOCKET_CONNECT, service);
				sessions.remove(session);
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
				}
			}
		}, 0, 180000);
	}
}