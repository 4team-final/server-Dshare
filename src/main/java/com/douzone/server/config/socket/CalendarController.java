package com.douzone.server.config.socket;


import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/cal")
public class CalendarController {
	private final CalendarService calendarService;

	@PostMapping
	public ResponseEntity<ResponseDTO> createRoom() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_CREATE_ROOM, calendarService.createRoom()));
	}

	@GetMapping
	public ResponseEntity<ResponseDTO> findAllRoom() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_LIST_ROOM, calendarService.findAllRoom()));
	}
}