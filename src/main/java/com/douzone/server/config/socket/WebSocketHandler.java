package com.douzone.server.config.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper;
	private final CalendarService calendarService;

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("{}", payload);

		System.out.println(session.getId());
		System.out.println(message);

//		objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

		TimeMessageDTO timeMessageDTO = objectMapper.readValue(payload, TimeMessageDTO.class);

		System.out.println(timeMessageDTO.getMessage());
		System.out.println(timeMessageDTO.getUid());
		System.out.println(timeMessageDTO.getTime());
		System.out.println(timeMessageDTO.getType());
		System.out.println(timeMessageDTO.getIsSeat());
		System.out.println(timeMessageDTO.getEmpNo());


		CalendarRoomDTO calendarRoomDTO = calendarService.findRoomById(timeMessageDTO.getUid());
		calendarRoomDTO.handlerActions(session, timeMessageDTO, calendarService);
	}
}
