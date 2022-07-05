package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.socket.CalendarRepository;
import com.douzone.server.exception.VehicleSocketServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import javax.persistence.LockModeType;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.douzone.server.exception.ErrorCode.TIME_TABLE_UPDATE_ERROR;


@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleSocketService {
	private final ObjectMapper objectMapper;
	private Map<String, VehicleRoomDTO> reservedRooms;
	private final CalendarRepository calendarRepository;
	private final TimeVehicleRepository timeVehicleRepository;

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

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Transactional
	@Lock(LockModeType.PESSIMISTIC_READ)
	public void updateIsSeat(Long vid, String uid, Integer[] time, String empNo) {
		Optional.ofNullable(uid)
				.map(v -> timeVehicleRepository.selectByUidAndVid(uid, vid))
				.filter(Optional::isPresent)
				.map(res -> {
					for (int i = 0; i < res.get().size(); i++) {
						if (time[i] == 1)
							res.get().get(i).updateTimeVehicle(time[i], empNo);
					}
					return res.get();
				})
				.orElseThrow(() -> new VehicleSocketServerException(TIME_TABLE_UPDATE_ERROR));
	}

	@Transactional
	public void updateIsSeat(Long vid, String startUid, String endUid, Integer startTime, Integer endTime, String empNo) {
		Optional.ofNullable(startUid)
				.map(v -> timeVehicleRepository.selectByUidAndVid(startUid, vid))
				.filter(Optional::isPresent)
				.map(res -> {
					for (int i = startTime; i < res.get().size(); i++)
						res.get().get(i).updateTimeVehicle(1, empNo);
					return res.get();
				})
				.map(v -> timeVehicleRepository.selectByUidAndVid(endUid, vid))
				.filter(Optional::isPresent)
				.map(res -> {
					for (int i = 0; i < endTime; i++)
						res.get().get(i).updateTimeVehicle(1, empNo);
					return res.get();
				})
				.orElseThrow(() -> new VehicleSocketServerException(TIME_TABLE_UPDATE_ERROR));
	}

	@Transactional(readOnly = true)
	public List<VehicleSocketResDTO> selectTime(String uid, Long vehicleId) {
		return Optional.ofNullable(uid)
				.map(v -> timeVehicleRepository.selectByUidAndVid(uid, vehicleId))
				.filter(Optional::isPresent)
				.map(res -> res.get().stream().map(time -> VehicleSocketResDTO.builder().build().of(time)).collect(Collectors.toList()))
				.orElseThrow(() -> new VehicleSocketServerException(TIME_TABLE_UPDATE_ERROR));
	}

	@Transactional
	public void resetIsSeat(Long vid, String uid, String empNo) {
		Optional.ofNullable(uid)
				.map(v -> timeVehicleRepository.selectByUidAndVid(uid, vid))
				.filter(Optional::isPresent)
				.map(res -> {
					for (int i = 0; i < res.get().size(); i++) {
						res.get().get(i).updateTimeVehicle(0, empNo);
					}
					return res.get();
				})
				.orElseThrow(() -> new VehicleSocketServerException(TIME_TABLE_UPDATE_ERROR));
	}
}
