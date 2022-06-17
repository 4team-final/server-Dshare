package com.douzone.server.service;

import com.douzone.server.dto.reservation.MyReservationResDTO;
import com.douzone.server.dto.reservation.ReservationResDTO;
import com.douzone.server.dto.reservation.RoomWeekReservationCountDTO;
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
	public List<ReservationResDTO> recentReservation(int limit) {
		List<RoomReservation> roomReservationList = reservationQueryDSL.findRecentReservation(limit);
		List<ReservationResDTO> reservationResDTOList = roomReservationList.stream().map(roomReservation -> {
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()));
			return reservationResDTO;
		}).collect(Collectors.toList());
		return reservationResDTOList;
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

		LocalDateTime soonStartTime = reservationQueryDSL.findBySoonStartTime(now, empId);
		LocalDateTime ingEndTime = reservationQueryDSL.findByIngEndTime(now, empId);
		SoonAndIngResDTO soonAndIngResDTO;

		// Long으로 보내기로 결정
		Long d_s = ChronoUnit.SECONDS.between(now, soonStartTime);
		Long d_i = ChronoUnit.SECONDS.between(now, ingEndTime);

		if (soonStartTime != null && ingEndTime != null) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(d_s, d_i);
		} else if (soonStartTime == null) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(null, d_i);
		} else if (ingEndTime == null) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(d_s, null);
		} else {
			return null;
		}
		return soonAndIngResDTO;
	}

	@Transactional
	public MyReservationResDTO myReservation(Long empId) {
		List<RoomReservation> beforeReservationList = reservationQueryDSL.findByBeforeReservation(empId);
		List<RoomReservation> afterReservationList = reservationQueryDSL.findByAfterReservation(empId);

		List<ReservationResDTO> beforeList = beforeReservationList.stream().map(roomReservation -> {
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()));
			return reservationResDTO;
		}).collect(Collectors.toList());

		List<ReservationResDTO> afterList = afterReservationList.stream().map(roomReservation -> {
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()));
			return reservationResDTO;
		}).collect(Collectors.toList());

		return MyReservationResDTO.builder().build().of(beforeList, afterList);

	}

	@Transactional
	public List<RoomWeekReservationCountDTO> weekReservationCount() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nowMinusWeek = now.minusDays(7);

		List<RoomWeekReservationCountDTO> roomWeekReservationCountDTOList = reservationQueryDSL.findByWeekReservationCount(now, nowMinusWeek);
		roomWeekReservationCountDTOList.stream().map(roomWeekReservationCountDTO -> {

			List<ReservationResDTO> reservationResDTOList = this.findByMeetingRoom_Id(roomWeekReservationCountDTO.getRoomId(), now, nowMinusWeek);

			roomWeekReservationCountDTO.setReservationResDTOList(reservationResDTOList);
			return roomWeekReservationCountDTO;
		}).collect(Collectors.toList());
		return roomWeekReservationCountDTOList;
	}

	@Transactional
	public List<ReservationResDTO> findByMeetingRoom_Id(Long roomId, LocalDateTime now, LocalDateTime nowMinusWeek) {
		List<RoomReservation> roomReservationList = reservationQueryDSL.findByMeetingRoomIdAndWeek(roomId, now, nowMinusWeek);
		List<ReservationResDTO> reservationResDTOList = roomReservationList.stream().map(roomReservation -> {
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()));
			return reservationResDTO;
		}).collect(Collectors.toList());
		return reservationResDTOList;
	}
}
