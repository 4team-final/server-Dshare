package com.douzone.server.config.socket;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class TimeService {

	private final ObjectMapper objectMapper;
	/**
	 * ObjectMapper란?
	 * JSON 컨텐츠를 Java 객체로 deserialization 하거나 Java 객체를 JSON으로 serialization 할 때 사용하는 Jackson 라이브러리의 클래스이다.
	 * ObjectMapper는 생성 비용이 비싸기 때문에 bean/static으로 처리하는 것이 좋다.
	 * <p>
	 * ObjectMapper를 이용하면 JSON을 Java 객체로 변환할 수 있고, 반대로 Java 객체를 JSON 객체로 serialization 할 수 있다.
	 */
	private Map<String, CalendarRoom> chatRooms;

	private final CalendarRepository calendarRepository;

	@PostConstruct
	private void init() {
		chatRooms = new LinkedHashMap<>();
	}


	public List<CalendarRoom> findAllRoom() {
		List<CalendarRoom> calendarRooms = new ArrayList<>(chatRooms.values());
		Collections.reverse(calendarRooms);

		return calendarRooms;
//		return testCalendarRepository.findAll();
	}

	public CalendarRoom findRoomByUId(String uId) {
		System.out.println("---------");
		System.out.println(chatRooms.get(uId));
		return chatRooms.get(uId);
//		return testCalendarRepository.findByUId(uId);
	}


	public CalendarRoom createRoom(String name) {
//		String randomId = UUID.randomUUID().toString();
		//2022-06-21T
		String uId = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
				.substring(0, 10).replace("-", "");

		CalendarRoom calendarRoom = CalendarRoom.builder()
				.uId(uId)
				.year(uId.substring(0, 4))
				.month(uId.substring(4, 6))
				.day(uId.substring(6, 8))
				.name(name)
				.build();
		chatRooms.put(uId, calendarRoom);
//		testCalendarRepository.save(calendarRoom);
		return calendarRoom;
	}

}



