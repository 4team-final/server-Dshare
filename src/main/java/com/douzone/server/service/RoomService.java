package com.douzone.server.service;

import com.douzone.server.dto.reservation.MyReservationResDTO;
import com.douzone.server.dto.reservation.ReservationResDTO;
import com.douzone.server.dto.reservation.RoomWeekReservationCountDTO;
import com.douzone.server.dto.reservation.SoonAndIngResDTO;
import com.douzone.server.dto.room.RoomBookmarkDTO;
import com.douzone.server.entity.RoomBookmark;
import com.douzone.server.entity.RoomReservation;
import com.douzone.server.repository.RoomRepository;
import com.douzone.server.repository.RoomReservationRepository;
import com.douzone.server.repository.querydsl.RoomQueryDSL;
import com.douzone.server.repository.querydsl.RoomReservationQueryDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final RoomReservationRepository roomReservationRepository;
	private final RoomReservationQueryDSL reservationQueryDSL;
	private final RoomQueryDSL roomQueryDSL;


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
		SoonAndIngResDTO soonAndIngResDTO;
		if (!roomReservationRepository.existsById(empId)) {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(0L, 0L);
			return soonAndIngResDTO;
		}
		LocalDateTime now = LocalDateTime.now();

		LocalDateTime soonStartTime = reservationQueryDSL.findBySoonStartTime(now, empId);
		LocalDateTime ingEndTime = reservationQueryDSL.findByIngEndTime(now, empId);

		// Long으로 보내기로 결정
		Long d_s;
		Long d_i;

		if (soonStartTime != null && ingEndTime != null) {
			d_s = ChronoUnit.SECONDS.between(now, soonStartTime);
			d_i = ChronoUnit.SECONDS.between(now, ingEndTime);
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(d_s, d_i);
		} else if (soonStartTime == null) {
			d_i = ChronoUnit.SECONDS.between(now, ingEndTime);
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(0L, d_i);
		} else if (ingEndTime == null) {
			d_s = ChronoUnit.SECONDS.between(now, soonStartTime);
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(d_s, 0L);
		} else {
			soonAndIngResDTO = SoonAndIngResDTO.builder().build().of(0L, 0L);
			return soonAndIngResDTO;
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

	@Transactional
	public List<ReservationResDTO> selectAllReservation() {

		List<ReservationResDTO> reservationResDTOList = roomQueryDSL.selectAllReservation().stream().map(roomReservation -> {
			return ReservationResDTO.builder().build().of(roomReservation);
		}).collect(Collectors.toList());

		return reservationResDTOList;
	}

	@Transactional
	public  List<ReservationResDTO> selectByRoomNoReservation(int roomNo) {
		return roomQueryDSL.selectRoomNoReservation(roomNo).stream().map(roomReservation -> {return ReservationResDTO.builder().build().of(roomReservation);}).collect(Collectors.toList());
	}

	@Transactional
	public List<ReservationResDTO> selectByDateRoomReservation(String startTime, String endTime) {
		return roomQueryDSL.selectDateTimeReservation(startTime, endTime).stream().map(roomReservation -> {return ReservationResDTO.builder().build().of(roomReservation);}).collect(Collectors.toList());
	}
	@Transactional
	public List<RoomBookmarkDTO> selectByLimitBookmark(int limit) {
		return	roomQueryDSL.selectTop3BookmarkMeetingRoom(limit);
	}
}
