package com.douzone.server.admin.service;

import com.douzone.server.admin.domain.vehicle.Vehicle;
import com.douzone.server.admin.domain.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle createReservation(Vehicle vehicle) {
        return
    }
}
