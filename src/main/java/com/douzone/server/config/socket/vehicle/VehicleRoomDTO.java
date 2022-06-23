package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.socket.Calendar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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

	public void handlerActions(WebSocketSession session, VehicleSocketReqDTO vehicleSocketReqDTO, VehicleSocketService service) {
		if (vehicleSocketReqDTO.getType().equals(VehicleSocketReqDTO.MessageType.ENTER)) {
			sessions.add(session);
			vehicleSocketReqDTO.setMessage(vehicleSocketReqDTO.getEmpNo() + "님이 입장했습니다.");
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

	public VehicleRoomDTO of(Calendar calendar) {
		return VehicleRoomDTO.builder()
				.uid(calendar.getUid())
				.build();
	}
}