package com.douzone.server.controller;


import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.reservation.RegistReservationReqDto;
import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * recentReservation(@RequestParam(value = "limit") @Valid int limit) - 최근에 예약된 회의실 TOP 조회
 * soonAndIngReservationMyTime(@AuthenticationPrincipal PrincipalDetails principalDetails) - 내가 예약한 회의실 남은 시간 조회 (회의전 - 곧 시작할,회의중 -회의가 끝나는)
 * myReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) - 내 예약 현황 조회 (과거 예약, 현재 예약)
 * myReservation(@PathVariable("empId") @Valid Long empId) - 특정 유저 예약 현황 조회
 * weekAndMonthReservationCount(@PathVariable("datetime") int datetime) - 7일/30일/90일 동안 많이 예약된 회의실별 개수, 회의실 목록 조회
 * weekAndMonthReservationCountHour(@PathVariable("datetime") int datetime) - 7일/30일/90일 동안 많이 예약된 시간별 개수 ,회의실 목록 조회
 * weekAndMonthMeetingCountHour(@PathVariable("datetime") int datetime) - 7일/30일/90일 동안 회의 시작 시간별 회의실 목록 조회
 * recentReservation() - 최근에 예약된 회의실 TOP 조회
 * soonAndIngReservationMyTime() - 내가 예약한 회의실 남은 시간 조회 (회의전 - 곧 시작할,회의중 -회의가 끝나는)
 * myReservation() - 내 예약 현황 조회 (과거 예약, 현재 예약)
 * selectByLimitBookmark() - 예약 즐겨찾기한 회의실 조회
 * selectByDateRoomReservation() - 특정시간대별 회의실 조회
 * selectByRoomNoReservation() - 회의실 호수에따른 예약 현황 조회
 * selectAllReservation() - 회의실 전체 예약 현황 조회
 */

@RestController
@Slf4j
@RequestMapping("/emp/room")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;


	@GetMapping("/reservation/recent")
	public ResponseEntity<ResponseDTO> recentReservation(@RequestParam(value = "limit") @Valid int limit) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RECENT, roomService.recentReservation(limit)));
	}

	@GetMapping("/reservation/soon/my/time")
	public ResponseEntity<ResponseDTO> soonAndIngReservationMyTime(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_SOON_MY_TIME, roomService.soonAndIngReservationMyTime(principalDetails.getEmployee().getId())));
	}

	@GetMapping("/reservation/my")
	public ResponseEntity<ResponseDTO> myReservation(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RESERVE_MY, roomService.myReservation(principalDetails.getEmployee().getId())));
	}

	@GetMapping("/reservation/{empId}")
	public ResponseEntity<ResponseDTO> myReservation(@PathVariable("empId") @Valid Long empId) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RESERVE_EMP, roomService.myReservation(empId)));
	}

	@GetMapping("/reservation/count/{datetime}")
	public ResponseEntity<ResponseDTO> weekAndMonthReservationCount(@PathVariable("datetime") int datetime) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_BEST_WEEK, roomService.weekAndMonthReservationCount(datetime)));
	}

	@GetMapping("/reservation/count/hour/{datetime}")
	public ResponseEntity<ResponseDTO> weekAndMonthReservationCountHour(@PathVariable("datetime") int datetime) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_BEST_DATE, roomService.weekAndMonthReservationCountHour(datetime)));
	}

	@GetMapping("/meeting/count/hour/{datetime}")
	public ResponseEntity<ResponseDTO> weekAndMonthMeetingCountHour(@PathVariable("datetime") int datetime) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_MEET_START, roomService.weekAndMonthMeetingCountHour(datetime)));
	}

	//페이지 네이션 해야함
	@GetMapping("/reservation/all")
	public ResponseEntity<ResponseDTO> selectAllReservation() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_FIND_ALL, roomService.selectAllReservation()));
	}

	// 동적 쿼리 : 호실, 인원수, 특정 시간대 합침.
	@GetMapping("/reservation/roomNo-capacity-time")
	public ResponseEntity<ResponseDTO> selectByRoomNoElseCapacityElseReservation(@RequestBody RoomReservationSearchDTO search) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_FIND_NO, roomService.selectByRoomNoElseCapacityElseReservation(search)));
	}//메세지 프로퍼티 활용 예정

	/**
	 * selectByDateRoomReservation() - 특정시간대별 회의실 조회
	 */
	@GetMapping("/reservation/time/{startTime}/{endTime}")
	public ResponseEntity<ResponseDTO> selectByDateRoomReservation(@PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_FIND_DATE, roomService.selectByDateRoomReservation(startTime, endTime)));
	}

	/**
	 * selectByLimitBookmark() - 즐겨찾기 상위 top(limit) 회의실 조회
	 **/
	@GetMapping("/reservation/my/bookmark/top/{limit}")
	public ResponseEntity<ResponseDTO> selectByLimitBookmark(@PathVariable("limit") int limit) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_FIND_MARK, roomService.selectByLimitBookmark(limit)));
	}

	/**
	 * 6/18 14:17 회의실 예약 오윤성
	 */
	@PostMapping("/regist")
	public ResponseEntity<ResponseDTO> saveReservation(@Validated @RequestBody RegistReservationReqDto registReservationReqDto) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RESERVE, roomService.save(registReservationReqDto)));
	}

	/**
	 * 6/19 18:51 회의실 예약 수정 오윤성
	 */
	@PostMapping("/my/update/{id}")
	public ResponseEntity<ResponseDTO> updateReservation(@Validated @RequestBody RegistReservationReqDto registReservationReqDto, @PathVariable("id") long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_UPDATE, roomService.update(registReservationReqDto, id)));
	}

	/**
	 * 6/19 18:51 회의실 예약 삭제 오윤성
	 */
	@GetMapping("/delete/{id}")
	public ResponseEntity<ResponseDTO> deleteReservation(@PathVariable("id") long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_DELETE, roomService.deleteRes(id)));
	}


}
