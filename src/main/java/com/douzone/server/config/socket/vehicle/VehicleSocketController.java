package com.douzone.server.config.socket.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/emp/vehicle/room")
public class VehicleSocketController {
	private final VehicleSocketService service;

	@GetMapping
	public List<VehicleRoomDTO> findAllRoom() {
		return service.findAllRoom();
	}
}
