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

	public void handlerActions(WebSocketSession session, VehicleSocketReqDTO vehicleSocketReqDTO, VehicleSocketService service) {
		if (vehicleSocketReqDTO.getType().equals(VehicleSocketReqDTO.MessageType.ENTER)) {
			sessions.add(session);
			vehicleSocketReqDTO.setMessage(vehicleSocketReqDTO.getSender() + "님이 입장했습니다.");
		} else if (vehicleSocketReqDTO.getType().equals(VehicleSocketReqDTO.MessageType.TALK)) {
			sendMessage(vehicleSocketReqDTO, service);
		} else if (vehicleSocketReqDTO.getType().equals(VehicleSocketReqDTO.MessageType.QUIT)) {
			sendMessage(vehicleSocketReqDTO, service);
		}

	}

	private <T> void sendMessage(T message, VehicleSocketService service) {
		sessions.parallelStream()
				.forEach(session -> service.sendMessage(session, message));
	}

	public Integer getSize() {
		return sessions.size();
	}
}