//package com.douzone.server.config.socket;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSocketTestHandler extends TextWebSocketHandler {
//
//	private final ObjectMapper objectMapper;
//	private final TimeService timeService;
//
//	@Override
//	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
//		String payload = message.getPayload();
//		log.info("payload {} ", payload);
////
////		TextMessage textMessage = new TextMessage("welcome~");
////		session.sendMessage(textMessage);
//
//		System.out.println(session);
//		System.out.println(message);
//
//		TimeMessage timeMessage = objectMapper.readValue(payload, TimeMessage.class);
//
//		CalendarRoom room = timeService.findRoomByUId(timeMessage.getUId());
//		room.handleActions(session, timeMessage, timeService);
//
//	}
//}
