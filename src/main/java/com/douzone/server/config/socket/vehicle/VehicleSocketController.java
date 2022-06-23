package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/vehicle/room")
public class VehicleSocketController {
	private final VehicleSocketService service;

	@PostMapping
	public ResponseEntity<ResponseDTO> createRoom(@RequestBody String name) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, service.createRoom(name)));
	}

	@GetMapping
	public List<VehicleSocketRoom> findAllRoom() {
		return service.findAllRoom();
	}
}
