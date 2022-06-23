package com.douzone.server.config.socket.vehicle;

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
public class VehicleSocketService {
	private final ObjectMapper objectMapper;
	private Map<String, VehicleSocketRoom> reservedRooms;

	@PostConstruct
	private void init() {
		reservedRooms = new LinkedHashMap<>();
	}

	public List<VehicleSocketRoom> findAllRoom() {
		return new ArrayList<>(reservedRooms.values());
	}

	public VehicleSocketRoom findRoomById(String roomId) {
		return reservedRooms.get(roomId);
	}

	public VehicleSocketRoom createRoom(String name) {
		String randomId = UUID.randomUUID().toString();
		VehicleSocketRoom vehicleSocketRoom = VehicleSocketRoom.builder()
				.roomId(randomId)
				.name(name)
				.build();
		reservedRooms.put(randomId, vehicleSocketRoom);
		return vehicleSocketRoom;
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
