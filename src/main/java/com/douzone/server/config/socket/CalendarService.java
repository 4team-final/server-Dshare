package com.douzone.server.config.socket;

import com.douzone.server.dto.employee.EmpResDTO;
import com.douzone.server.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarService {
	private final ObjectMapper objectMapper;
	private Map<String, CalendarRoomDTO> calRooms;
	private final CalendarRepository calendarRepository;
	private final TimeRepository timeRepository;
	private final EmployeeRepository employeeRepository;

	@PostConstruct
	private void init() {
		calRooms = new LinkedHashMap<>();
		List<CalendarRoomDTO> calendarRoomDTOList = calendarRepository.findAll().stream()
				.map(calendar -> {
					CalendarRoomDTO calendarRoomDTO = CalendarRoomDTO.builder().build().of(calendar);
					return calendarRoomDTO;
				}).collect(Collectors.toList());

		for (int i = 0; i < calendarRoomDTOList.size(); i++) {
			calRooms.put(calendarRoomDTOList.get(i).getUid(), calendarRoomDTOList.get(i));
		}

		//
		
	}

	@Transactional
	public List<CalendarRoomDTO> findAllRoom() {
		List<CalendarRoomDTO> calendarRoomDTOList = new ArrayList<>(calRooms.values());
		for (int i = 0; i < calendarRoomDTOList.size(); i++) {
			int size = calendarRoomDTOList.get(i).getSessions().size();
			Set<WebSocketSession> sessions = calendarRoomDTOList.get(i).getSessions();
			Iterator<WebSocketSession> res = sessions.iterator();
			while (res.hasNext()) {
				Principal principal = res.next().getPrincipal();
				String name = principal.getName();
				calendarRoomDTOList.get(i).getEmpResDTOList().add(EmpResDTO.builder().build().of(employeeRepository.findByEmpNo(name)));

			}
//			calendarRoomDTOList.get(i).setEmpResDTOList(calendarRoomDTOList.get(i).getSessions().);
			calendarRoomDTOList.get(i).setPeopleNum(size);
		}


//		List<CalendarRoomDTO> calendarRoomDTOList = calendarRepository.findAll().stream()
//				.map(calendar -> {
//					CalendarRoomDTO calendarRoomDTO = CalendarRoomDTO.builder().build().of(calendar);
//					return calendarRoomDTO;
//				}).collect(Collectors.toList());

		return calendarRoomDTOList;
	}

	public CalendarRoomDTO findRoomById(String uid) {

		return calRooms.get(uid);
//		return CalendarRoomDTO.builder().build().of(calendarRepository.findById(uid).get());
	}

	@Transactional
	public CalendarRoomDTO createRoom() {
//		String randomId = UUID.randomUUID().toString();
		//2022-06-21T


		String uid = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
				.substring(0, 10).replace("-", "");
		CalendarRoomDTO calendarRoomDTO = CalendarRoomDTO.builder()
				.uid(uid)
				.build();

//		calRooms.put(uid, calendarRoomDTO);

		calendarRepository.save(Calendar.builder()
				.uid(calendarRoomDTO.getUid())
				.build());

		return calendarRoomDTO;
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
//			objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Transactional
	public void updateTime(String uid, Integer[] time, String empNo) {

		List<Time> timeList = timeRepository.findByCalendar_Uid(uid);

		for (int i = 0; i < timeList.size(); i++) {
			if (time[i] == 0) continue;
			timeList.get(i).updateIsSeat(time[i], empNo);
		}
		log.info("updateTime - success");

	}

	@Transactional
	public List<TimeMessageResDTO> selectTime(String uid) {
		List<TimeMessageResDTO> timeMessageResDTOList = timeRepository.findByCalendar_Uid(uid)
				.stream().map(time -> {
					return TimeMessageResDTO.builder().build().of(time);
				}).collect(Collectors.toList());

		log.info("selectTime - success");

		return timeMessageResDTOList;
	}
}

