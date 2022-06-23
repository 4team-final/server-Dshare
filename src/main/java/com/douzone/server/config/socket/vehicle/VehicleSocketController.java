package com.douzone.server.config.socket.vehicle;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/vehicle/room")
public class VehicleSocketController {
	private final VehicleSocketService service;

	@PostMapping
	public ResponseEntity<ResponseDTO> createRoom() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, service.createRoom()));
	}

	@GetMapping
	public List<VehicleRoomDTO> findAllRoom() {
		return service.findAllRoom();
	}
}
