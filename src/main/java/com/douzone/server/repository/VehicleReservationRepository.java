package com.douzone.server.repository;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long>{
//    VehicleReservation save(VehicleReservation vehicleReservation);
//    long findById(VehicleReservation vehicleReservation);
//    void deleteById(long id);
@Query("select vr from VehicleReservation vr left join Vehicle v on vr.vehicle.id = v.id")
List<VehicleReservation> findAllReserved();
}
