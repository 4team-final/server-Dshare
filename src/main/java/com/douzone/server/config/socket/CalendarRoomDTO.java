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

import java.io.IOException;
import java.util.*;

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

	public void handlerActions(WebSocketSession session, TimeMessageReqDTO timeMessageReqDTO, CalendarService calendarService, TimeService timeService) {

		if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.ENTER)) {

			String empNo = session.getPrincipal().getName();
			Iterator<WebSocketSession> res = sessions.iterator();
			while (res.hasNext()) {
				String compareEmpNo = res.next().getPrincipal().getName();
				if (compareEmpNo.equals(empNo)) {
					sendMessage(session, FAIL_DOUBLE_ACCESS_SOCKET_CONNECT, calendarService);
					return;
				}
			}


			sessions.add(session);
			sendMessage(timeMessageReqDTO.getEmpNo() + " " + VehicleSocketDTO.MessageType.ENTER, calendarService);
			autoDisconnect(session, calendarService); // 시간
			this.close(session);
		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.TALK)) {

			timeService.updateTime(timeMessageReqDTO.getUid(), timeMessageReqDTO.getTime(), timeMessageReqDTO.getEmpNo(), timeMessageReqDTO.getRoomId());
			List<TimeMessageResDTO> resDTOList = timeService.selectTime(timeMessageReqDTO.getUid());
			sendMessage(resDTOList, calendarService);
			sessions.remove(session);
			this.close(session);

		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.QUIT)) {
			// 시간 - 날짜  : 버튼 (다시 날짜를 선택하게)
			TimeMessageResDTO timeMessageResDTO = TimeMessageResDTO.builder()
					.uid(timeMessageReqDTO.getUid())
					.empNo(timeMessageReqDTO.getEmpNo())
					.build();

			sendMessage(timeMessageResDTO, calendarService);
			sessions.remove(session);
			this.close(session);
		} else {
			if (sessions.contains(session)) {
				sendMessage(session, FAIL_ACCESS_SOCKET_CONNECT, calendarService);
				sessions.remove(session);
				this.close(session);
				return;
			}
			sendMessage(session, FAIL_ACCESS_SOCKET_TYPE, calendarService);
			this.close(session);
		}
	}

	private void close(WebSocketSession session) {
		try {
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
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
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				int i = 60;

				@Override
				public void run() {
					i--;
					if (i < 0) {
						sendMessage(session, TIMEOUT_CONNECT_VEHICLE_SOCKET, calendarService);
						sessions.remove(session);
						try {
							session.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						timer.cancel();
					}
				}
			}, 0, 1000);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}
}