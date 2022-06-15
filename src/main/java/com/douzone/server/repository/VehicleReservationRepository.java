package com.douzone.server.repository;

import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {

	@Query("select vr from VehicleReservation vr left join Employee e on vr.employee.id = e.id where e.id = :id and vr.endedAt < :date")
	List<VehicleReservation> findEmpBefore(@Param("id") Long id, @Param("date") Date date);

	@Query("select vr from VehicleReservation vr left join Employee e on vr.employee.id = e.id where e.id = :id and vr.startedAt > :date")
	List<VehicleReservation> findEmpAfter(@Param("id") Long id, @Param("date") Date date);
}