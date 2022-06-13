package com.douzone.server.admin.controller;


import com.douzone.server.admin.dto.employee.SignupReqDTO;
import com.douzone.server.admin.service.AdminService;
import com.douzone.server.config.utils.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<ResponseDTO> signup(@RequestBody SignupReqDTO signupReqDTO) {
        return null;
    }


}
