package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * recentReservation() - 최근에 예약된 회의실 TOP 조회
 * soonAndIngReservationMyTime() - 내가 예약한 회의실 남은 시간 조회 (회의전 - 곧 시작할,회의중 -회의가 끝나는)
 * myReservation() - 내 예약 현황 조회 (과거 예약, 현재 예약)
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
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RESERVE_MY, roomService.myReservation(empId)));
	}

}
