package com.douzone.server.repository;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {
    // vr.id as id,
    @Query("select vr.id as id, vr.startedAt as dateTime " +
            "from VehicleReservation vr " +
            "join fetch Vehicle v on vr.vehicle.id = v.id " +
            "join fetch Employee e on vr.employee.id = e.id " +
            "where e.id = :empId and dateTime > current_time")
    IVehicleTimeResDTO soonReservationMyTime(@Param("empId") Long empId, Pageable pageable);

    @Query("select vr.id as id, vr.endedAt as endedAt " +
            "from VehicleReservation vr " +
            "join fetch Vehicle v on vr.vehicle.id = v.id " +
            "join fetch Employee e on vr.employee.id = e.id " +
            "where e.id = :empId and vr.startedAt < current_timestamp and current_timestamp < endedAt")
    VehicleReservation ingReservationMyTime(Long empId, Pageable pageable);
}
