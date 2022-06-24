package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.employee.SignModReqDTO;
import com.douzone.server.dto.employee.mod;
import com.douzone.server.dto.employee.modPw;
import com.douzone.server.dto.employee.signUp;
import com.douzone.server.dto.room.RoomReqDTO;
import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.dto.vehicle.VehicleUpdateDTO;
import com.douzone.server.service.AdminService;
import com.douzone.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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

	private final AdminService adminService;
	private final RoomService roomService;

	/**
	 * 사원등록 - 정재빈
	 */
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> register(@RequestBody @Validated(signUp.class) SignModReqDTO signModReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_REGISTER, adminService.register(signModReqDTO)));
	}

	/**
	 * 사원수정 - 오윤성
	 */
	@PostMapping("/update/{id}")
	public ResponseEntity<ResponseDTO> update(@RequestBody @Validated(mod.class) SignModReqDTO signModReqDTO, @PathVariable("id") long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_MOD, adminService.update(signModReqDTO, id)));
	}

	/**
	 * 사원 비밀번호 수정 - 오윤성
	 */
	@PostMapping("/updatePw/{id}")
	public ResponseEntity<ResponseDTO> updatePw(@RequestBody @Validated(modPw.class) SignModReqDTO signModReqDTO, @PathVariable("id") long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_MODPW, adminService.updatePw(signModReqDTO, id)));
	}

	/**
	 * 직급별/팀별/부서별/사원번호별/사원이름별 유저의 회의실 예약 조회 - 관리자
	 */
	@GetMapping("/reservation/read/various")
	public ResponseEntity<ResponseDTO> VariousSearch(@RequestBody RoomReservationSearchDTO search) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_RESERVE_USER, adminService.searchVarious(search)));
	}

	@GetMapping("/check")
	public ResponseEntity<ResponseDTO> check() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_REGISTER, "admin이 아니면 통과 못합니다."));
	}

	@PostMapping("/image/upload")
	public ResponseEntity<ResponseDTO> uploadProfileImg(@NotNull List<MultipartFile> files, long TargetEmpId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, adminService.uploadProfileImg(files, TargetEmpId)));
	}

	/**
	 * 차량 정보 등록
	 */
	@PostMapping("/creation/vehicle")
	public ResponseEntity<ResponseDTO> createVehicle(@Valid VehicleUpdateDTO vehicleUpdateDTO, @NotNull List<MultipartFile> files) {
		return ResponseEntity.ok().body(adminService.createVehicle(vehicleUpdateDTO, files));
	}

	/**
	 * 차량 정보 수정
	 */
	@PatchMapping("/modification/vehicle")
	public ResponseEntity<ResponseDTO> updateVehicle(@Valid VehicleUpdateDTO vehicleUpdateDTO, @RequestParam("id") Long id, @NotNull List<MultipartFile> files) {
		return ResponseEntity.ok().body(adminService.updateVehicle(vehicleUpdateDTO, id, files));
	}

	/**
	 * 차량 정보 삭제
	 */
	@DeleteMapping("/elimination/vehicle")
	public ResponseEntity<ResponseDTO> deleteVehicle(@RequestParam("id") Long id) {
		return ResponseEntity.ok().body(adminService.deleteVehicle(id));
	}

	/**
	 * 회의실 등록
	 */

	@PostMapping("/room")
	public ResponseEntity<ResponseDTO> register(@NotNull List<MultipartFile> files, @Valid RoomReqDTO roomReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM, roomService.register(files, roomReqDTO)));
	}

	/**
	 * 회의실 삭제
	 */
	@DeleteMapping("/room/{roomId}")
	public ResponseEntity<ResponseDTO> delete(@PathVariable("roomId") long roomId) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_DELETE, roomService.delete(roomId)));
	}

	/**
	 * 회의실 수정
	 */
	@PutMapping("/room/{roomId}")
	public ResponseEntity<ResponseDTO> update(@NotNull List<MultipartFile> files, @PathVariable("roomId") long roomId, RoomReqDTO roomReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_UPDATE, roomService.update(files, roomId, roomReqDTO)));
	}

}
