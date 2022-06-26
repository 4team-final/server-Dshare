package com.douzone.server.service;


import com.douzone.server.config.utils.UploadDTO;
import com.douzone.server.config.utils.UploadUtils;
import com.douzone.server.dto.reservation.*;
import com.douzone.server.dto.room.*;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomImg;
import com.douzone.server.entity.RoomObject;
import com.douzone.server.entity.RoomReservation;
import com.douzone.server.exception.*;
import com.douzone.server.repository.RoomImgRepository;
import com.douzone.server.repository.RoomObjectRepository;
import com.douzone.server.repository.RoomRepository;
import com.douzone.server.repository.RoomReservationRepository;
import com.douzone.server.repository.querydsl.RoomQueryDSL;
import com.douzone.server.repository.querydsl.RoomReservationQueryDSL;
import com.douzone.server.service.method.ServiceMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
	private final ServiceMethod serviceMethod;


	@Transactional
	public List<ReservationResDTO> recentReservation(int limit) {
		List<RoomReservation> roomReservationList = reservationQueryDSL.findRecentReservation(limit);
		List<ReservationResDTO> reservationResDTOList = roomReservationList.stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);

			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation,
					timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());
		return reservationResDTOList;
	}

	public LocalDateTime timeDiff(LocalDateTime startedAt, LocalDateTime endedAt) {
		long diffMinute = ChronoUnit.MINUTES.between(startedAt, endedAt);
		LocalDateTime now = LocalDateTime.now();
		return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), (int) ((diffMinute % 1440) / 60), (int) (diffMinute % 60));
	}

	@Transactional
	public SoonAndIngResDTO soonAndIngReservationMyTime(Long empId) {
		SoonAndIngResDTO soonAndIngResDTO;
		if (!roomReservationRepository.existsByEmployee_Id(empId)) {
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
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());

		List<ReservationResDTO> afterList = afterReservationList.stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());

		return MyReservationResDTO.builder().build().of(beforeList, afterList);
	}

	//페이징
	@Transactional
	public List<ReservationPagingRes> myReservation(Long empId, long lastId, int limit) {
		List<RoomReservation> ReservationList = roomQueryDSL.selectAllReservationPage(lastId, limit, empId);
		long total = roomQueryDSL.countReservation(empId);
		List<ReservationPagingRes> MyList = ReservationList.stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return new ReservationPagingRes().of(reservationResDTO, total, limit, lastId);
		}).collect(Collectors.toList());
		return MyList;
	}


	public LocalDateTime now() {
		return LocalDateTime.now();
	}

	@Transactional
	public List<WeekCountHourResDTO> weekAndMonthReservationCount(int datetime) {

		LocalDateTime now = this.now();
		LocalDateTime nowMinusWeek = now.minusDays(datetime);

		List<WeekCountHourResDTO> weekCountResDTOList = reservationQueryDSL.findByWeekAndMonthReservationCount(now, nowMinusWeek);
		weekCountResDTOList.stream().map(WeekCountHourResDTO -> {

			List<ReservationResDTO> reservationResDTOList = this.findByMeetingRoom_Id(WeekCountHourResDTO.getRoomId(), now, nowMinusWeek);

			reservationResDTOList.stream().map(reservationResDTO -> {
				List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(reservationResDTO);
				reservationResDTO.getRoom().setRoomObjectResDTOList((List<RoomObjectResDTO>) twoList.get(0));
				reservationResDTO.getRoom().setRoomImgResDTOList((List<RoomImgResDTO>) twoList.get(1));
				return reservationResDTO;
			}).collect(Collectors.toList());

			WeekCountHourResDTO.setReservationResDTOList(reservationResDTOList);
			return WeekCountHourResDTO;
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
			reservationResDTOList.stream().map(reservationResDTO -> {
				List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(reservationResDTO);
				reservationResDTO.getRoom().setRoomObjectResDTOList((List<RoomObjectResDTO>) twoList.get(0));
				reservationResDTO.getRoom().setRoomImgResDTOList((List<RoomImgResDTO>) twoList.get(1));
				return reservationResDTO;
			}).collect(Collectors.toList());

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

			reservationResDTOList.stream().map(reservationResDTO -> {
				List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(reservationResDTO);
				reservationResDTO.getRoom().setRoomObjectResDTOList((List<RoomObjectResDTO>) twoList.get(0));
				reservationResDTO.getRoom().setRoomImgResDTOList((List<RoomImgResDTO>) twoList.get(1));
				return reservationResDTO;
			}).collect(Collectors.toList());

			weekCountHourResDTO.setReservationResDTOList(reservationResDTOList);
			return weekCountHourResDTO;
		}).collect(Collectors.toList());
		return weekCountHourResDTOList;
	}

	@Transactional
	public List<ReservationResDTO> selectAllReservation() {
		List<ReservationResDTO> reservationResDTOList = roomQueryDSL.selectAllReservation().stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation,
					timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());

		return reservationResDTOList;
	}

	/**
	 * 예약 전체 조회 페이징
	 *
	 * @return
	 */
	@Transactional
	public List<ReservationPagingRes> selectAllReservation(long lastId, int limit) {
		long total = roomQueryDSL.countReservation();
		List<ReservationPagingRes> reservationPagingResList = roomQueryDSL.selectAllReservationPage(lastId, limit).stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation,
					timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return new ReservationPagingRes().of(reservationResDTO, total, limit, lastId);
		}).collect(Collectors.toList());
		//총갯수 추가
		return reservationPagingResList;
	}

	@Transactional
	public List<ReservationResDTO> selectByRoomNoElseCapacityElseReservation(RoomReservationSearchDTO search) {
		return roomQueryDSL.selectByRoomNoElseCapacityElseReservation(search).stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation,
					timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());
	}

	@Transactional
	public List<ReservationResDTO> selectByDateRoomReservation(String startTime, String endTime) {
		return roomQueryDSL.selectDateTimeReservation(startTime, endTime).stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation,
					timeDiff(roomReservation.getStartedAt(), roomReservation.getEndedAt()), (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());
	}

	@Transactional
	public List<RoomResDTO> selectByLimitBookmark(int limit) {
		List<RoomBookmarkResDTO> roomBookmarkResDTOList = roomQueryDSL.selectTop3BookmarkMeetingRoom(limit);
		return serviceMethod.RoomImgListAndRoomObjectList(roomBookmarkResDTOList);
	}

	@Transactional
	public Long register(List<MultipartFile> files, RoomReqDTO roomReqDTO) {
		String basePath = "room/";

		//회의실 등록
		long roomId = roomRepository.save(roomReqDTO.of()).getId();
		//회의실 물품 등록
		List<Long> roomObjectInsertIdList = roomReqDTO.getRoomObjects().stream().map(roomObjectReqDTO -> {
			long id = roomObjectRepository.save(RoomObjectReqDTO.builder().build().of(roomId, roomObjectReqDTO)).getId();
			return id;
		}).collect(Collectors.toList());
		//회의실 이미지 등록
		List<UploadDTO> uploadDTOS = uploadUtils.upload(files, basePath);
		List<Long> roomImgInsertIdList = uploadDTOS.stream().map(uploadDTO -> {
			long id = roomImgRepository.save(UploadDTO.builder().build().room_of(roomId, uploadDTO)).getId();
			return id;
		}).collect(Collectors.toList());

		if (uploadDTOS == null) {
			throw new ImgFileNotFoundException(ErrorCode.IMG_NOT_FOUND);
		}
		return roomId;
	}

	@Transactional
	public Long delete(long roomId) {
		List<RoomObject> roomObjectList = roomObjectRepository.findByMeetingRoom_Id(roomId);
		if (roomObjectList == null) {
			throw new RoomObjectNotFoundException(ErrorCode.ROOM_OBJECT_NOT_FOUND);
		}
		List<Long> roomObjectDeleteIdList = roomObjectList.stream().map(roomObject -> {
			roomObjectRepository.deleteById(roomObject.getId());
			return roomObject.getId();
		}).collect(Collectors.toList());

		List<RoomImg> roomImgList = roomImgRepository.findByMeetingRoom_Id(roomId);
		List<String> CurrentImgPath = roomImgRepository.findPathByRoomId(roomId);
		if (roomImgList == null) {
			throw new RoomImgNotFoundException(ErrorCode.ROOM_OBJECT_NOT_FOUND);
		}
		roomImgList.stream().map(roomImg -> {
			roomImgRepository.deleteById(roomImg.getId());
			return roomImg.getId();
		}).collect(Collectors.toList());
		//이미지 경로
		uploadUtils.delete(CurrentImgPath);


		Optional<MeetingRoom> room = Optional.ofNullable(roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(ErrorCode.ROOM_NOT_FOUND)));
		roomRepository.delete(room.get());

		return room.get().getId();
	}

	@Transactional
	public Long update(@NotNull List<MultipartFile> files, long roomId, RoomReqDTO roomReqDTO) {
		String basePath = "room/";

		// 회의실 물건들 수정
		// 회의실 수정
		// 회의실 사진들 수정
		List<RoomObject> roomObjectList = roomObjectRepository.findByMeetingRoom_Id(roomId);

		for (int i = 0; i < roomObjectList.size(); i++) {
			roomObjectList.get(i).updateRoomObject(roomReqDTO.getRoomObjects().get(i).getName());
		}
		//수정할때 룸 오브젝트를 추가 하고 싶지만 못할 수 있다. 애초에 갯수를 5개로 정해둠

		Optional<MeetingRoom> room = Optional.ofNullable(roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(ErrorCode.ROOM_NOT_FOUND)));
		room.get().updateRoom(roomReqDTO.getContent(), roomReqDTO.getCategoryName(), roomReqDTO.getRoomNo(), roomReqDTO.getCapacity());

		// 회의실 사진 삭제 후 저장
		List<RoomImg> roomImgList = roomImgRepository.findByMeetingRoom_Id(roomId);
		List<String> CurrentImgPath = roomImgRepository.findPathByRoomId(roomId);
		if (roomImgList.size()==0) {
			throw new RoomImgNotFoundException(ErrorCode.ROOM_OBJECT_NOT_FOUND);
		}
//		-> 회의실 사진이 없을경우 나머지도 수정안되고 오류던지고 끝난다.
		roomImgList.stream().map(roomImg -> {
			roomImgRepository.deleteById(roomImg.getId());
			return roomImg.getId();
		}).collect(Collectors.toList());
		uploadUtils.delete(CurrentImgPath);


		// 회의실 이미지 등록
		//파일 업로드시 아무 파일을 업로드하지 않아도 리스트에 뭔가가 들어있음 -> 그래서 이상한 파일이 올라감
		long count = files.stream().filter(t->!t.isEmpty()).count();
		//-> 리스트를 까서 file이 빈파일이 아닌것만 센다. 이렇게 하면 아무것도 안넘겼을 시 0이 나온다.
		//-> System.out.println(files.size()); 하면 1이 나온다. 디폴트로
		if(count != 0) {
			List<UploadDTO> uploadDTOS = uploadUtils.upload(files, basePath);
			uploadDTOS.stream().map(uploadDTO -> {
				long id = roomImgRepository.save(UploadDTO.builder().build().room_of(roomId, uploadDTO)).getId();
				return id;
			}).collect(Collectors.toList());
		}
		return roomId;
	}

	@Transactional
	public Long save(RegistReservationReqDto registReservationReqDto) {

		// 타임 테이블에도 반영해줘야함


		return roomReservationRepository.save(RoomReservation.builder().build().of(registReservationReqDto)).getId();
	}

	@Transactional
	public Long update(RegistReservationReqDto registReservationReqDto, long id) {

		// 타임 테이블에 반영해야함

		RoomReservation roomReservation = roomReservationRepository.findById(id).orElseThrow(() -> new reservationNotFoundException(ErrorCode.RES_NOT_FOUND));
		roomReservation.updateReservation(
				registReservationReqDto.getRoomId(),
				registReservationReqDto.getReason(),
				registReservationReqDto.getTitle(),
				registReservationReqDto.getStartedAt(),
				registReservationReqDto.getEndedAt()
		);
		return roomReservation.getId();
	}

	@Transactional
	public Long deleteRes(long id) {
		RoomReservation roomReservation = roomReservationRepository.findById(id).orElseThrow(() -> new reservationNotFoundException(ErrorCode.RES_NOT_FOUND));
		roomReservationRepository.deleteById(id);
		return roomReservation.getId();
	}
}
