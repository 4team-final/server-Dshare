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

//    @Autowired
    VehicleRepository vehicleRepository;
    @PostMapping("/createReservation")
    public Vehicle createVehicleReservation(@Valid @RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @PostMapping(/)
    pub

    @PostMapping("/bookmark")
    public Vehicle vehicleBookmark(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
