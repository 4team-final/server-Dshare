package com.douzone.server.repository;

import com.douzone.server.entity.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {
	@Modifying
	@Query(value = "update vehicle_reservation set modifiedAt = :modifiedAt ,createdAt = :createdAt where id= :id", nativeQuery = true)
	void TestUpdateModified( @Param("modifiedAt")LocalDateTime modifiedAt,@Param("createdAt") LocalDateTime createdAt,@Param("id")Long id);

	@Query(value = "select rr.startedAt from vehicle_reservation rr where id= :id", nativeQuery = true)
	String TestStartedAt(@Param("id")Long id);

}
