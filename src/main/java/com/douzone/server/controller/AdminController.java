package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Message;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.service.AdminService;
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

	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> register(@RequestBody @Valid SignupReqDTO signupReqDTO) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Message.SUCCESS_ADMIN_REGISTER, adminService.register(signupReqDTO)));
	}

	@GetMapping("/check")
	public ResponseEntity<ResponseDTO> check() {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Message.SUCCESS_ADMIN_REGISTER, "admin이 아니면 통과 못합니다."));
	}

	@PostMapping("/image/upload")
	public ResponseEntity<ResponseDTO> uploadProfileImg(@NotNull List<MultipartFile> files, long TargetEmpId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Message.SUCCESS_ADMIN_PROFILEIMG, adminService.uploadProfileImg(files, TargetEmpId)));
	}
}
