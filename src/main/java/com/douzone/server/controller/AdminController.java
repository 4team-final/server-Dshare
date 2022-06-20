package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.dto.room.RoomReqDTO;
import com.douzone.server.dto.vehicle.VehicleUpdateDTO;
import com.douzone.server.service.AdminService;
import com.douzone.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

	private static final String METHOD_NAME = VehicleController.class.getName();
	private final AdminService adminService;
	private final RoomService roomService;

	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> register(@RequestBody @Valid SignupReqDTO signupReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_REGISTER, adminService.register(signupReqDTO)));
	}

	@GetMapping("/check")
	public ResponseEntity<ResponseDTO> check() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_REGISTER, "admin이 아니면 통과 못합니다."));
	}

	@PostMapping("/image/upload")
	public ResponseEntity<ResponseDTO> uploadProfileImg(@NotNull List<MultipartFile> files, long TargetEmpId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, adminService.uploadProfileImg(files, TargetEmpId)));
	}

	@PostMapping("/creation/vehicle")
	public ResponseEntity<ResponseDTO> createVehicle(@RequestBody @Valid VehicleUpdateDTO vehicleUpdateDTO, @NotNull List<MultipartFile> files) {
		return ResponseEntity.ok().body(adminService.createVehicle(vehicleUpdateDTO, files));
	}

	@PatchMapping("/modification/vehicle")
	public ResponseEntity<ResponseDTO> updateVehicle(@RequestBody @Valid VehicleUpdateDTO vehicleUpdateDTO, @RequestParam("id") Long id, List<MultipartFile> files) {
		return ResponseEntity.ok().body(adminService.updateVehicle(vehicleUpdateDTO, id, files));
	}

	@DeleteMapping("/elimination/vehicle")
	public ResponseEntity<ResponseDTO> deleteVehicle(@RequestBody Long id) {
		return ResponseEntity.ok().body(adminService.deleteVehicle(id));
	}

	@PostMapping("/room")
	public ResponseEntity<ResponseDTO> register(@NotNull List<MultipartFile> files, @Valid RoomReqDTO roomReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM, roomService.register(files, roomReqDTO)));
	}

	@DeleteMapping("/room/{roomId}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable("roomId") long roomId) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_DELETE, roomService.delete(roomId)));
	}

	@PutMapping("/room/{roomId}")
	public ResponseEntity<ResponseDTO> update(@NotNull List<MultipartFile> files, @PathVariable("roomId") long roomId, RoomReqDTO roomReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_UPDATE, roomService.update(files, roomId, roomReqDTO)));
	}
}
