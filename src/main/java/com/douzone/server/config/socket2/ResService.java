package com.douzone.server.config.socket2;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResService {
	private final ObjectMapper objectMapper;
	private Map<String, TimeRoom> timeRooms;

	@PostConstruct
	private void init() {
		timeRooms = new LinkedHashMap<>();
	}

	public List<TimeRoom> findAllRoom() {
		return new ArrayList<>(timeRooms.values());
	}

	public TimeRoom findRoomById(String roomId) {
		return timeRooms.get(roomId);
	}

	public TimeRoom createRoom(TimeRoom timeroom) {
		String randomId = "20220623";
		TimeRoom timeRoom = TimeRoom.builder()
				.uId(randomId)
				.time(timeroom.getTime())
				.isSeat(timeroom.getIsSeat())
				.empNo(timeroom.getEmpNo())
				.name(timeroom.getName())
				.build();
		timeRooms.put(randomId, timeRoom);
		return timeRoom;
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try{
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}

