package com.douzone.server.config.socket;

import com.douzone.server.dto.employee.EmpResDTO;
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
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendarRoomDTO {
	private String uid;

	@JsonIgnore
	private Set<WebSocketSession> sessions = new HashSet<>();

	private int peopleNum;

	private List<EmpResDTO> empResDTOList;

	@Builder
	public CalendarRoomDTO(String uid) {
		this.uid = uid;
	}

	public void handlerActions(WebSocketSession session, TimeMessageDTO timeMessageDTO, CalendarService calendarService) {
		if (timeMessageDTO.getType().equals(TimeMessageDTO.MessageType.ENTER)) {
			sessions.add(session);
			timeMessageDTO.setMessage(timeMessageDTO.getEmpNo() + "님이 입장했습니다.");
		} else if (timeMessageDTO.getType().equals(TimeMessageDTO.MessageType.TALK)) {
			sendMessage(timeMessageDTO, calendarService);
		} else if (timeMessageDTO.getType().equals(TimeMessageDTO.MessageType.QUIT)) {
			sendMessage(timeMessageDTO, calendarService);
		}

	}

	private <T> void sendMessage(T message, CalendarService calendarService) {
		sessions.parallelStream()
				.forEach(session -> calendarService.sendMessage(session, message));
	}

	public CalendarRoomDTO of(Calendar calendar) {
		return CalendarRoomDTO.builder()
				.uid(calendar.getUid())
				.build();
	}
}