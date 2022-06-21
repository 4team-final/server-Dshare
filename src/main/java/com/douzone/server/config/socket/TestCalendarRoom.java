package com.douzone.server.config.socket;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class TestCalendarRoom {

	private String uId;
	private String name;
	private Set<WebSocketSession> sessions = new HashSet<>();

	@Builder
	public TestCalendarRoom(String uId, String name) {
		this.uId = uId;
		this.name = name;
	}

	public void handleActions(WebSocketSession session, TestTimeMessage testTimeMessage, TimeService timeService) {
		if (testTimeMessage.getType().equals(TestTimeMessage.MessageType.ENTER)) {
			sessions.add(session);
			testTimeMessage.setMessage(testTimeMessage.getEmpName() + "님이 " + testTimeMessage.getUId() + " 선택하여 입장");
		}
		sendMessage(testTimeMessage, timeService);
	}

	public <T> void sendMessage(T message, TimeService chatService) {
//		sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
	}
}
