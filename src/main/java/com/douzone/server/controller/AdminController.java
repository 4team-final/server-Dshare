package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.employee.SignModReqDTO;
import com.douzone.server.dto.employee.signUp;
import com.douzone.server.dto.employee.mod;
import com.douzone.server.dto.employee.modPw;
import com.douzone.server.dto.reservation.ReservationResDTO;
import com.douzone.server.dto.room.RoomImgResDTO;
import com.douzone.server.dto.room.RoomObjectResDTO;
import com.douzone.server.dto.room.RoomReqDTO;
import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.repository.querydsl.RoomQueryDSL;
import com.douzone.server.service.AdminService;
import com.douzone.server.service.RoomService;
import com.douzone.server.service.method.ServiceMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

	private static final String METHOD_NAME = VehicleController.class.getName();
	private final AdminService adminService;
	private final RoomService roomService;
	//테스트
	private final RoomQueryDSL roomQueryDSL;
	private final ServiceMethod serviceMethod;
	private MessageSource msg;

	/**
	 *  사원등록 - 정재빈
	 */
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> register(@RequestBody @Validated(signUp.class) SignModReqDTO signModReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_REGISTER, adminService.register(signModReqDTO)));
	}
	/**
	 *  사원수정 - 오윤성
	 */
	@PostMapping("/update/{id}")
	public ResponseEntity<ResponseDTO> update(@RequestBody @Validated(mod.class) SignModReqDTO signModReqDTO ,@PathVariable("id")long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_MOD, adminService.update(signModReqDTO, id)));
	}

	/**
	 *  사원 비밀번호 수정 - 오윤성
	 */
	@PostMapping("/updatePw/{id}")
	public ResponseEntity<ResponseDTO> updatePw(@RequestBody @Validated(modPw.class) SignModReqDTO signModReqDTO, @PathVariable("id")long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_MODPW, adminService.updatePw(signModReqDTO, id)));
	}

	/**
	 *팀별/부서별/사원번호별/사원이름별 유저의 회의실 예약 조회 - 관리자
	 */
	@GetMapping("/reservation/read/various")
	public ResponseEntity<ResponseDTO> VariousSearch(@RequestBody RoomReservationSearchDTO search) {
		log.info("search : {} , {}, {}, {}", search.getTeamNo(), search.getDeptNo(), search.getEmpNo(), search.getEmpName());
		List<ReservationResDTO> list = roomQueryDSL.selectByVariousColumns(search).stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK,  Msg.SUCCESS_ADMIN_MODPW, list));
	}


	@GetMapping("/check")
	public ResponseEntity<ResponseDTO> check() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_REGISTER, "admin이 아니면 통과 못합니다."));
	}

	@PostMapping("/image/upload")
	public ResponseEntity<ResponseDTO> uploadProfileImg(@NotNull List<MultipartFile> files, long TargetEmpId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, adminService.uploadProfileImg(files, TargetEmpId)));
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
