package com.douzone.server.config.socket;

import com.douzone.server.config.socket.vehicle.VehicleSocketDTO;
import com.douzone.server.dto.employee.EmpResDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.douzone.server.config.utils.Msg.*;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
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

		if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.ENTER)) {
			sessions.add(session);
			autoDisconnect(session, calendarService); // 시간
			sendMessage(timeMessageReqDTO.getEmpNo() + VehicleSocketDTO.MessageType.ENTER, calendarService);

		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.TALK)) {

			calendarService.updateTime(timeMessageReqDTO.getUid(), timeMessageReqDTO.getTime(), timeMessageReqDTO.getEmpNo());
			List<TimeMessageResDTO> resDTOList = calendarService.selectTime(timeMessageReqDTO.getUid());
			sendMessage(resDTOList, calendarService);
			sessions.remove(session);

		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.QUIT)) {
			// 시간 - 날짜  : 버튼 (다시 날짜를 선택하게)
			TimeMessageResDTO timeMessageResDTO = TimeMessageResDTO.builder()
					.uid(timeMessageReqDTO.getUid())
					.empNo(timeMessageReqDTO.getEmpNo())
					.build();

			sendMessage(timeMessageResDTO, calendarService);
			sessions.remove(session);

		} else {
			// find sesstion
			if (sessions.contains(session)) {
				sessions.remove(session);
				sendMessage(session, FAIL_ACCESS_SOCKET_CONNECT, calendarService);
				return;
			}
			sendMessage(session, FAIL_ACCESS_SOCKET_TYPE, calendarService);
		}
	}


	private <T> void sendMessage(T message, CalendarService calendarService) {
		sessions.parallelStream()
				.forEach(session -> calendarService.sendMessage(session, message));
	}

	private <T> void sendMessage(WebSocketSession s, T message, CalendarService calendarService) {
		calendarService.sendMessage(s, message);
	}


	public CalendarRoomDTO of(Calendar calendar) {
		return CalendarRoomDTO.builder()
				.uid(calendar.getUid())
				.build();
	}

	public void autoDisconnect(WebSocketSession session, CalendarService calendarService) {
		try {
			Thread.sleep(180000);
			sendMessage(session, TIMEOUT_CONNECT_VEHICLE_SOCKET, calendarService);
			sessions.remove(session);
		} catch (InterruptedException ie) {
			log.error(FAIL_TIMEOUT_SETTING_SOCKET, ie);
		}
	}
}