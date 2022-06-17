package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Message;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.room.RoomBookmarkDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	@GetMapping("/test")
	public ResponseEntity<ResponseDTO> queryDSLTest(@RequestParam(value = "positionId") long positionId) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK,
				Message.SUCCESS_ADMIN_REGISTER, employeeService.queryDSLTest(positionId)));
	}

	@GetMapping("/profile/read")
	public ResponseEntity<ResponseDTO> readProfile(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK,
				Msg.SUCCESS_EMP_PROFILE, employeeService.readProfile(principalDetails.getEmployee().getId())));
	}

	/**
	 * 6/17 19:55 나의 즐겨찾기 테이블 조회(회의실, 사원정보까지 줄줄이 소세지)
	 */
	@GetMapping("/room/bookmark")
	public ResponseEntity<ResponseDTO> selectByMyBookmark(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK,
				Msg.SUCCESS_ROOM_FIND_MARK, employeeService.selectByMyBookmark(Integer.parseInt(principalDetails.getEmployee().getEmpNo()))));
	}


}
