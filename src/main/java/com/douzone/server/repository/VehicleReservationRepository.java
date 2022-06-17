package com.douzone.server.repository;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {
    @Query("select vr " +
            "from VehicleReservation vr " +
//            "join fetch Vehicle v on vr.vehicle.id = v.id " +
//            "join fetch Employee e on vr.employee.id = e.id " +
            "where vr.id = :vrId")
    VehicleReservation findVehicleReserved(Long vrId);
}
