package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.socket.Calendar;
import com.douzone.server.config.socket.CalendarRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleSocketService {
	private final ObjectMapper objectMapper;
	private Map<String, VehicleRoomDTO> reservedRooms;
	private final CalendarRepository calendarRepository;

	@PostConstruct
	private void init() {
		reservedRooms = new LinkedHashMap<>();
		List<VehicleRoomDTO> vehicleRoomDTOS = calendarRepository.findAll()
				.stream()
				.map(v -> VehicleRoomDTO.builder().build().of(v))
				.collect(Collectors.toList());
		for (VehicleRoomDTO vehicleRoomDTO : vehicleRoomDTOS)
			reservedRooms.put(vehicleRoomDTO.getUid(), vehicleRoomDTO);
	}

	public List<VehicleRoomDTO> findAllRoom() {
		List<VehicleRoomDTO> vehicleRoomDTOS = new ArrayList<>(reservedRooms.values());
		for (VehicleRoomDTO vehicleRoomDTO : vehicleRoomDTOS) {
			List<String> empNoList = new ArrayList<>();
			Set<WebSocketSession> sessions = vehicleRoomDTO.getSessions();
			for (WebSocketSession session : sessions) {
				empNoList.add(Objects.requireNonNull(session.getPrincipal()).getName());
			}
			vehicleRoomDTO.setPeopleNum(vehicleRoomDTO.getSessions().size());
			vehicleRoomDTO.setEmpNoList(empNoList);
		}

		return vehicleRoomDTOS;
	}

	public VehicleRoomDTO findRoomById(String uid) {
		return reservedRooms.get(uid);
	}

	public VehicleRoomDTO createRoom() {
		String uid = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)
				.substring(0, 10).replace("-", "");
		VehicleRoomDTO vehicleRoomDTO = VehicleRoomDTO.builder()
				.uid(uid)
				.build();
		calendarRepository.save(Calendar.builder()
				.uid(vehicleRoomDTO.getUid())
				.build());
		return vehicleRoomDTO;
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
