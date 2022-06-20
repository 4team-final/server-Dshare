package com.douzone.server.repository;

import com.douzone.server.dto.vehicle.IVehicleTimeResDTO;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {

}
