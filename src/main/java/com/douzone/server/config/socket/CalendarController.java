package com.douzone.server.config.socket;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/calendar")
public class CalendarController {

	private final TimeService timeService;
	private final SimpMessageSendingOperations messageSendingOperations;


	/**
	 * 날짜방 등록
	 */
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO> createRoom(@RequestParam("name") String name) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_CREATE_ROOM, timeService.createRoom(name)));
	}

	@GetMapping("/list")
	public ResponseEntity<ResponseDTO> findAllRoom() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_LIST_ROOM, timeService.findAllRoom()));
	}

	@GetMapping("/list/{uId}")
	public ResponseEntity<ResponseDTO> roomDetail(@PathVariable("uId") String uId) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_DETAIL_ROOM, timeService.findRoomByUId(uId)));
	}

}
