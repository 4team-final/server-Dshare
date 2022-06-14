package com.douzone.server.controller;


import com.douzone.server.config.utils.Message;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.service.AdminService;
import com.douzone.server.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@PreAuthorize("")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 6/14 : 관리자 사원등록
     * 정재빈
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> signup(@RequestBody @Valid SignupReqDTO signupReqDTO) {
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Message.SUCCESS_ADMIN_REGISTER, adminService.register(signupReqDTO)));
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseDTO> check() {
        return ResponseEntity.ok().body(ResponseDTO.of(HttpStatus.OK, Message.SUCCESS_ADMIN_REGISTER, "admin이 아니면 통과 못해합니다."));
    }

    @PostMapping("/vehicle/create_reservation")
    public ResponseDTO createVehicleReservation(@RequestBody VehicleReservationDTO vehicleReservationDTO) {
        Integer result = adminService.createVehicleReservation(vehicleReservationDTO);
        if(result == 0) return new ResponseDTO().fail(HttpStatus.BAD_REQUEST, "");

        return new ResponseDTO().of(HttpStatus.OK, "");
    }
}
