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
import java.time.temporal.ChronoUnit;
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

		// Long으로 보내기로 결정
		Long d_s = ChronoUnit.SECONDS.between(now, soonTime);
		Long d_i = ChronoUnit.SECONDS.between(now, ingEndTime);

		if (soonTime != null && ingEndTime != null) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(d_s, d_i);
		} else if (soonTime == null) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(null, d_i);
		} else if (ingEndTime == null) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(d_s, null);
		} else {
			return null;
		}
		return soonAndIngResDTO;
	}

}
