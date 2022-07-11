package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.service.AdminService;
import com.douzone.server.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/emp")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;
	private final AdminService adminService;

//	@GetMapping("/test")
//	public ResponseEntity<ResponseDTO> queryDSLTest(@RequestParam(value = "positionId") long positionId) {
//		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK,
//				Msg.SUCCESS_ADMIN_REGISTER, employeeService.queryDSLTest(positionId)));
//	}
	/**
	 * 부서, 팀, 포지션 조회
	 */
	@GetMapping("/team/read/{deptId}")
	public ResponseEntity<ResponseDTO> teamRead(@PathVariable("deptId")long deptId) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_EMP_PROFILE, employeeService.readTeamInfo(deptId)));
	}
	@GetMapping("/dept/read")
	public ResponseEntity<ResponseDTO> deptRead() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_EMP_PROFILE, employeeService.readDeptInfo()));
	}
	@GetMapping("/position/read")
	public ResponseEntity<ResponseDTO> positionRead() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_EMP_PROFILE, employeeService.readPositionInfo()));
	}



	/**
	 * 사원 이미지 수정, 사원 + 관리자 모두 가능
	 */
	@PostMapping("/image/upload")
	public ResponseEntity<ResponseDTO> uploadProfileImg(@NotNull List<MultipartFile> files, long TargetEmpId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ADMIN_PROFILEIMG, adminService.uploadProfileImg(files, TargetEmpId)));
	}

	/**
	 * 프로필 읽어오기
	 */
	@GetMapping("/profile/read")
	public ResponseEntity<ResponseDTO> readProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_EMP_PROFILE, employeeService.readProfile(principalDetails.getEmployee().getId())));
	}
	@GetMapping("/profile/read/{id}")
	public ResponseEntity<ResponseDTO> readProfile(@PathVariable Long id) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_EMP_PROFILE, employeeService.readProfile(id)));
	}
	/**
	 * 전체 조회
	 */
	@GetMapping("/profile/all/read")
	public ResponseEntity<ResponseDTO> readProfile() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_EMP_PROFILE, employeeService.readProfile()));
	}


	/**:
	 * 6/17 19:55 나의 즐겨찾기 테이블 조회(회의실, 사원정보까지 줄줄이 소세지)
	 */
	@GetMapping("/my/bookmark")
	public ResponseEntity<ResponseDTO> selectByMyBookmark(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK,
				Msg.SUCCESS_ROOM_FIND_MARK, employeeService.selectByMyBookmark(Integer.parseInt(principalDetails.getEmployee().getEmpNo()))));
	}

	/**
	 * 즐겨찾기 등록 및 삭제
	 */
	@PostMapping("/room/bookmark/{roomId}")
	public ResponseEntity<ResponseDTO> bookmarkRegisterAndDelete(@PathVariable("roomId") long roomId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		Long result = employeeService.bookmarkRegisterAndDelete(roomId, principalDetails.getEmployee().getId());
		if (result == 1) {
			return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_BOOKMARK));

		} else {
			return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_ROOM_DELETE_MARK));

		}
	}

	@GetMapping("/ws/validation")
	public ResponseEntity<ResponseDTO> selectByMyEmployeeNumber(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_SELECT_EMP_NO, principalDetails.getEmployee().getEmpNo()));
	}
}
