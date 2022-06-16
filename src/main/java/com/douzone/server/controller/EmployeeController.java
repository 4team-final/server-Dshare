package com.douzone.server.controller;

import com.douzone.server.config.security.auth.PrincipalDetails;
import com.douzone.server.config.utils.Message;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


}
