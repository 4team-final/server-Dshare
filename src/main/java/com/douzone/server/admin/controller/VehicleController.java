package com.douzone.server.admin.controller;

import com.douzone.server.admin.domain.vehicle.Vehicle;
import com.douzone.server.admin.domain.vehicle.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//@Setter
@RequestMapping("/api/vehicle")
public class VehicleController {
//        id 예약 고유번호
//        vehicleId 차량 고유번호
//        empId 사원 고유번호
//        reason 예약 사유
//        title 예약 이름
//        startedAt 차량대여 시작시간
//        endedAt 차량대여 종료시간
//        createdAt 예약 생성 날짜
//        modifiedAt 예약 수정 날짜

//    @Autowired
    VehicleReservationRepository vehicleReservationRepository;
    VehicleRepository vehicleRepository;

    @PostMapping("/reservation/save")
    public VehicleResrvation saveVehicleReservation(@Valid @RequestBody Reservation reservation) {
        return vehicleReservationRepository.save(reservation);
    }

    @PostMapping("/bookmark")
    public Vehicle vehicleBookmark(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    // 강제 등록?
    @PostMapping(/)
    pub
}
