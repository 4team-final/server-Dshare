package com.douzone.server.service;

import com.douzone.server.dto.reservation.RecentResDTO;
import com.douzone.server.dto.reservation.SoonAndIngResDTO;
import com.douzone.server.entity.RoomReservation;
import com.douzone.server.repository.RoomRepository;
import com.douzone.server.repository.RoomReservationRepository;
import com.douzone.server.repository.querydsl.RoomReservationQueryDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomReservationRepository roomReservationRepository;
	private final RoomReservationQueryDSL reservationQueryDSL;

	@Transactional
	public List<RecentResDTO> recentReservation(int limit) {
		List<RoomReservation> roomReservationList = reservationQueryDSL.findRecentReservation(limit);
		List<RecentResDTO> recentResDTOList = roomReservationList.stream().map(roomReservation -> {
			RecentResDTO recentResDTO = RecentResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()));
			return recentResDTO;
		}).collect(Collectors.toList());
		return recentResDTOList;
	}

	public LocalDateTime timeDiff(LocalDateTime startedAt, LocalDateTime endedAt) {
		return endedAt.minusHours(startedAt.getHour()).minusMinutes(startedAt.getHour());
	}

	@Transactional
	public SoonAndIngResDTO soonAndIngReservationMyTime(Long empId) {

		if (!roomReservationRepository.existsById(empId)) {
			return null;
		}
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime soonTime = reservationQueryDSL.findByUserSoonReservationTime(now, empId);
		LocalDateTime ingEndTime = reservationQueryDSL.findByUserEndReservationTime(now, empId);
		SoonAndIngResDTO soonAndIngResDTO;
		LocalDateTime soonRemainTime;
		LocalDateTime ingRemainTime;


		if (soonTime != null && ingEndTime != null) {
			soonRemainTime = now.minusHours(soonTime.getHour()).minusMinutes(soonTime.getMinute());
			ingRemainTime = ingEndTime.minusHours(now.getHour()).minusMinutes(now.getMinute());
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(soonRemainTime, ingRemainTime);

		} else if (soonTime == null) {
			ingRemainTime = ingEndTime.minusHours(now.getHour()).minusMinutes(now.getMinute());
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(null, ingRemainTime);

		} else if (ingEndTime == null) {
			soonRemainTime = now.minusHours(soonTime.getHour()).minusMinutes(soonTime.getMinute());
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(soonRemainTime, null);
		} else {
			return null;
		}
		return soonAndIngResDTO;
	}
}
