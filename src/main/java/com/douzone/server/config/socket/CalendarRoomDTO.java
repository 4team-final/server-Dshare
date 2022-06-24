package com.douzone.server.config.socket;

import com.douzone.server.dto.employee.EmpResDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
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

	private List<EmpResDTO> empResDTOList = new ArrayList<>();

	@Builder
	public CalendarRoomDTO(String uid) {
		this.uid = uid;
	}

	public void handlerActions(WebSocketSession session, TimeMessageReqDTO timeMessageReqDTO, CalendarService calendarService) {
		TimeMessageResDTO timeMessageResDTO = new TimeMessageResDTO();

		if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.ENTER)) {
			sessions.add(session);
//			timeMessageReqDTO.setMessage(timeMessageReqDTO.getEmpNo() + "님이 입장했습니다.");

		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.TALK)) {
			System.out.println(timeMessageReqDTO.getEmpNo());
			for (int i = 0; i < timeMessageReqDTO.getTime().length; i++) {
				System.out.println(timeMessageReqDTO.getTime()[i]);
			}
			calendarService.updateTime(timeMessageReqDTO.getUid(), timeMessageReqDTO.getTime(), timeMessageReqDTO.getEmpNo());


			sendMessage(timeMessageResDTO, calendarService);
		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.QUIT)) {
			sendMessage(timeMessageResDTO, calendarService);
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