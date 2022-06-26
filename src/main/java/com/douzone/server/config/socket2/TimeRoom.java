package com.douzone.server.config.socket2;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class TimeRoom {
	private String uId;
	private String name;
	private String time;
	private Integer isSeat;
	private String empNo;

	private Set<WebSocketSession> sessions = new HashSet<>();

	@Builder
	public TimeRoom(String uId, String name, String time, Integer isSeat, String empNo) {
		this.uId = uId;
		this.name = name;
		this.time = time;
		this.isSeat = isSeat;
		this.empNo = empNo;
	}




	public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ResService resService) {

		if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
			sessions.add(session);
			chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
		}else if(chatMessage.getType().equals(ChatMessage.MessageType.TALK)){
			sendMessage(chatMessage, resService);
		}else if(chatMessage.getType().equals(ChatMessage.MessageType.QUIT)){
			sendMessage(chatMessage, resService);
		}

	}

	private <T> void sendMessage(T message, ResService resService) {
		sessions.parallelStream()
				.forEach(session -> resService.sendMessage(session, message));
	}
}