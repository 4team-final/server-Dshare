package com.douzone.server.service;


import com.douzone.server.config.utils.UploadDTO;
import com.douzone.server.config.utils.UploadUtils;
import com.douzone.server.dto.reservation.*;
import com.douzone.server.dto.room.RoomBookmarkDTO;
import com.douzone.server.dto.room.RoomObjectReqDTO;
import com.douzone.server.dto.room.RoomReqDTO;
import com.douzone.server.entity.RoomReservation;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.exception.ImgFileNotFoundException;
import com.douzone.server.repository.RoomImgRepository;
import com.douzone.server.repository.RoomObjectRepository;
import com.douzone.server.repository.RoomRepository;
import com.douzone.server.repository.RoomReservationRepository;
import com.douzone.server.repository.querydsl.RoomQueryDSL;
import com.douzone.server.repository.querydsl.RoomReservationQueryDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	private final UploadUtils uploadUtils;
	private final RoomImgRepository roomImgRepository;
	private final RoomObjectRepository roomObjectRepository;
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

	public LocalDateTime now() {
		return LocalDateTime.now();
	}

	@Transactional
	public List<WeekCountResDTO> weekAndMonthReservationCount(int datetime) {

		LocalDateTime now = this.now();
		LocalDateTime nowMinusWeek = now.minusDays(datetime);

		List<WeekCountResDTO> weekCountResDTOList = reservationQueryDSL.findByWeekAndMonthReservationCount(now, nowMinusWeek);
		weekCountResDTOList.stream().map(weekCountResDTO -> {

			List<ReservationResDTO> reservationResDTOList = this.findByMeetingRoom_Id(weekCountResDTO.getRoomId(), now, nowMinusWeek);

			weekCountResDTO.setReservationResDTOList(reservationResDTOList);
			return weekCountResDTO;
		}).collect(Collectors.toList());
		return weekCountResDTOList;
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
	public List<WeekCountHourResDTO> weekAndMonthReservationCountHour(int datetime) {

		LocalDateTime now = this.now();
		LocalDateTime nowMinusWeek = now.minusDays(datetime);

		List<WeekCountHourResDTO> weekCountHourResDTOList = reservationQueryDSL.findByWeekAndMonthReservationCountHour(now, nowMinusWeek);
		weekCountHourResDTOList.stream().map(weekCountHourResDTO -> {

			List<ReservationResDTO> reservationResDTOList = this.findByMeetingRoom_Id(weekCountHourResDTO.getRoomId(), now, nowMinusWeek);

			weekCountHourResDTO.setReservationResDTOList(reservationResDTOList);
			return weekCountHourResDTO;
		}).collect(Collectors.toList());
		return weekCountHourResDTOList;
	}

	@Transactional
	public List<WeekCountHourResDTO> weekAndMonthMeetingCountHour(int datetime) {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nowMinusWeek = now.minusDays(datetime);

		List<WeekCountHourResDTO> weekCountHourResDTOList = reservationQueryDSL.findByWeekAndMonthMeetingCountHour(now, nowMinusWeek);
		weekCountHourResDTOList.stream().map(weekCountHourResDTO -> {

			List<ReservationResDTO> reservationResDTOList = this.findByMeetingRoom_Id(weekCountHourResDTO.getRoomId(), now, nowMinusWeek);

			weekCountHourResDTO.setReservationResDTOList(reservationResDTOList);
			return weekCountHourResDTO;
		}).collect(Collectors.toList());
		return weekCountHourResDTOList;
	}

	@Transactional
	public Long register(List<MultipartFile> files, RoomReqDTO roomReqDTO) {
		String basePath = "room/";

		//회의실 등록
		long roomId = roomRepository.save(roomReqDTO.of()).getId();
		//회의실 물품 등록
		List<Long> roomObjectIdList = roomReqDTO.getRoomObjects().stream().map(roomObjectReqDTO -> {
			long id = roomObjectRepository.save(RoomObjectReqDTO.builder().build().of(roomId, roomObjectReqDTO)).getId();
			return id;
		}).collect(Collectors.toList());
		//회의실 이미지 등록
		List<UploadDTO> uploadDTOS = uploadUtils.upload(files, basePath);
		List<Long> roomImgIdList = uploadDTOS.stream().map(uploadDTO -> {
			long id = roomImgRepository.save(UploadDTO.builder().build().room_of(roomId, uploadDTO)).getId();
			return id;
		}).collect(Collectors.toList());

		if (uploadDTOS == null) {
			throw new ImgFileNotFoundException(ErrorCode.IMG_NOT_FOUND);
		}

		return roomId;
	}

	public List<ReservationResDTO> selectAllReservation() {

		List<ReservationResDTO> reservationResDTOList = roomQueryDSL.selectAllReservation().stream().map(roomReservation -> {
			return ReservationResDTO.builder().build().of(roomReservation);
		}).collect(Collectors.toList());

		return reservationResDTOList;
	}

	@Transactional
	public List<ReservationResDTO> selectByRoomNoReservation(int roomNo) {
		return roomQueryDSL.selectRoomNoReservation(roomNo).stream().map(roomReservation -> {
			return ReservationResDTO.builder().build().of(roomReservation);
		}).collect(Collectors.toList());
	}

	@Transactional
	public List<ReservationResDTO> selectByDateRoomReservation(String startTime, String endTime) {
		return roomQueryDSL.selectDateTimeReservation(startTime, endTime).stream().map(roomReservation -> {
			return ReservationResDTO.builder().build().of(roomReservation);
		}).collect(Collectors.toList());
	}

	@Transactional
	public List<RoomBookmarkDTO> selectByLimitBookmark(int limit) {
		return roomQueryDSL.selectTop3BookmarkMeetingRoom(limit);
	}
}
