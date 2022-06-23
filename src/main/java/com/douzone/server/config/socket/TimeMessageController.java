package com.douzone.server.config.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TimeMessageController {

	private final SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/emp/cal/message")
	public void message(TimeMessage message) {
		if (TimeMessage.MessageType.ENTER.equals(message.getType()))
			message.setMessage(message.getEmpName() + "님이 입장하셨습니다.");
		messagingTemplate.convertAndSend("/sub/cal/room/" + message.getUId(), message);
	}
}
