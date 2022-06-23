package com.douzone.server.config.socket.vehicle;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class VehicleSocketRoom {
	private final String roomId;
	private final String name;
	private final Set<WebSocketSession> sessions = new HashSet<>();

	@Builder
	public VehicleSocketRoom(String roomId, String name) {
		this.roomId = roomId;
		this.name = name;
	}

	public void handlerActions(WebSocketSession session, VehicleSocketDTO vehicleSocketDTO, VehicleSocketService service) {
		if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.ENTER)) {
			sessions.add(session);
			vehicleSocketDTO.setMessage(vehicleSocketDTO.getSender() + "님이 입장했습니다.");
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.TALK)) {
			sendMessage(vehicleSocketDTO, service);
		} else if (vehicleSocketDTO.getType().equals(VehicleSocketDTO.MessageType.QUIT)) {
			sendMessage(vehicleSocketDTO, service);
		}

	}

	private <T> void sendMessage(T message, VehicleSocketService service) {
		sessions.parallelStream()
				.forEach(session -> service.sendMessage(session, message));
	}
}