package com.douzone.server.config.socket;

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
public class TestCalendarRoom {

	private String uId;
	private String name;
	private String year;
	private String month;
	private String day;

	private Set<WebSocketSession> sessions = new HashSet<>();
	private List<TestTimeMessage> testTimeMessageList = new ArrayList<>();

	@Builder
	public TestCalendarRoom(String uId, String name, String year, String month, String day) {
		this.uId = uId;
		this.name = name;
		this.year = year;
		this.month = month;
		this.day = day;
	}


	public void handleActions(WebSocketSession session, TestTimeMessage testTimeMessage, TimeService timeService) {
		if (testTimeMessage.getType().equals(TestTimeMessage.MessageType.ENTER)) {
			sessions.add(session);
			testTimeMessage.setMessage(testTimeMessage.getEmpName() + "님이 " + testTimeMessage.getUId() + " 선택하여 입장");
		}
		sendMessage(testTimeMessage, timeService);
	}

	public <T> void sendMessage(T message, TimeService timeService) {
		sessions.parallelStream().forEach(session -> timeService.sendMessage(session, message));
	}

	public TestCalendarRoom of(Calendar calendar) {
		return TestCalendarRoom.builder()
				.uId(calendar.getUId())
				.year(calendar.getYear())
				.month(calendar.getMonth())
				.day(calendar.getDay())
				.name(calendar.getName())
				.build();
	}

	public Calendar of() {
		return Calendar.builder()
				.uId(uId)
				.year(year)
				.month(month)
				.day(day)
				.name(name)
				.build();
	}
}
