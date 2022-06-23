package com.douzone.server.config.socket;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/calendar")
public class TestCalendarController {
	private TimeService timeService;

	/**
	 * 날짜방 등록
	 */
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createRoom(@RequestParam("name") String name) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_CREATE_ROOM, timeService.createRoom(name)));
	}

	@GetMapping("/list")
	public ResponseEntity<ResponseDTO> findAllRoom() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_CREATE_ROOM, timeService.findAllRoom()));
	}


}
