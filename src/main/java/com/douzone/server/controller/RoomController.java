package com.douzone.server.controller;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/emp/room")
@RequiredArgsConstructor
public class RoomController {

	private final RoomService roomService;

	@GetMapping("/recent/reservation")
	public ResponseEntity<ResponseDTO> recentReservation(@RequestParam(value = "limit") @Valid int limit) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RECENT, roomService.recentReservation(limit)));
	}
}
