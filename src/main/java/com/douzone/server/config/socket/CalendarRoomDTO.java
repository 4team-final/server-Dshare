package com.douzone.server.config.socket;

import com.douzone.server.dto.employee.EmpResDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
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
			sessions.add(session);
			List<TimeMessageResDTO> resDTOList = timeService.selectTime(timeMessageReqDTO.getUid(), timeMessageReqDTO.getRoomId(), TimeMessageReqDTO.MessageType.ENTER);

			SocketResDTO socketResDTO = SocketResDTO.builder()
					.results(resDTOList)
					.message(timeMessageReqDTO.getEmpNo() + " 사번의 사원이 " + timeMessageReqDTO.getUid() + " 날짜의 " + timeMessageReqDTO.getRoomId() + " 번 회의실을 구경중입니다.")
					.build();

			sendMessage(socketResDTO, calendarService);
			autoDisconnect(session, calendarService);
		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.TALK)) {

			timeService.updateTime(timeMessageReqDTO.getUid(), timeMessageReqDTO.getTime(), timeMessageReqDTO.getEmpNo(), timeMessageReqDTO.getRoomId());
			List<TimeMessageResDTO> resDTOList = timeService.selectTime(timeMessageReqDTO.getUid(), timeMessageReqDTO.getRoomId(), TimeMessageReqDTO.MessageType.ENTER);
			SocketResDTO socketResDTO = SocketResDTO.builder()
					.results(resDTOList)
					.message(timeMessageReqDTO.getEmpNo() + " 사번의 사원이 " + timeMessageReqDTO.getUid() + " 날짜의 " + timeMessageReqDTO.getRoomId() + " 번 회의실을 선점하였습니다.")
					.build();
			sendMessage(socketResDTO, calendarService);
//			sessions.remove(session);
//			this.close(session);
		} else if (timeMessageReqDTO.getType().equals(TimeMessageReqDTO.MessageType.QUIT)) {

			//여기는 isSeat 변환점이 있는지 확인하고 , 다시 타임테이블 되돌리는 로직이 필요함

			// 시간 - 날짜  : 버튼 (다시 날짜를 선택하게)

			SocketResDTO socketResDTO = SocketResDTO.builder()
					.message(timeMessageReqDTO.getEmpNo() + " 사번의 사원이 " + timeMessageReqDTO.getUid() + "방에서 접속을 종료하였습니다.")
					.build();
			sendMessage(socketResDTO, calendarService);
			sessions.remove(session);
			this.close(session);
		} else {
			if (sessions.contains(session)) {
				sendMessage(session,
						SocketResDTO.builder()
								.message(FAIL_ACCESS_SOCKET_CONNECT)
								.build(),
						calendarService);
				sessions.remove(session);
				this.close(session);
				return;
			}
			sendMessage(session,
					SocketResDTO.builder()
							.message(FAIL_ACCESS_SOCKET_TYPE)
							.build(),
					calendarService);
			this.close(session);
		}
	}

	@Synchronized
	private void close(WebSocketSession session) {
		try {
			if (session.isOpen()) {
				session.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private <T> void sendMessage(T message, CalendarService calendarService) {
		sessions.parallelStream()
				.forEach(session -> {
					if (session.isOpen()) {
						calendarService.sendMessage(session, message);
					}
				});
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
				int i = 180;

				@Override
				public void run() {
					i--;
					if (i < 0) {
						sendMessage(session, TIMEOUT_CONNECT_VEHICLE_SOCKET, calendarService);
						sessions.remove(session);

						close(session);

						timer.cancel();
					}
				}
			}, 0, 1000);

		} catch (Exception e) {
			log.info(e.getMessage());
		}
	}
}