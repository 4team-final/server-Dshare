package com.douzone.server.config.socket2;


import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/chat")
public class ChatController {
	private final ResService resService;

	@PostMapping
	public ResponseEntity<ResponseDTO> createRoom(@RequestBody TimeRoom timeroom) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, resService.createRoom(timeroom)));
	}

	@GetMapping
	public List<TimeRoom> findAllRoom() {
		return resService.findAllRoom();
	}
}