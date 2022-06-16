package com.douzone.server.repository;

import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long>{
}
