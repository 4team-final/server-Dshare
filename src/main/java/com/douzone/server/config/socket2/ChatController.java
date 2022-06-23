package com.douzone.server.config.socket2;


import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//채팅방과 관련된 비즈니스 로직을 담은 Service
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/chat")
public class ChatController {
	private final ChatService chatService;

	@PostMapping
	public ResponseEntity<ResponseDTO> createRoom(@RequestBody String name) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, chatService.createRoom(name)));
	}

	@GetMapping
	public List<ChatRoom> findAllRoom() {
		return chatService.findAllRoom();
	}
}