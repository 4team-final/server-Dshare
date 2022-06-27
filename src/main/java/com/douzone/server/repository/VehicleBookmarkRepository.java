package com.douzone.server.repository;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleBookmarkRepository extends JpaRepository<VehicleBookmark, Long> {

	@Query("select v from VehicleBookmark vb " +
			"join fetch Vehicle v on vb.vehicle.id = v.id " +
			"where vb.employee.empNo = :empNo")
	List<Vehicle> findByMyBookMarkVehicle(@Param("empNo") String empNo);

	@Query("select v, count(vb) as vcount " +
			"from VehicleBookmark vb " +
			"join fetch Vehicle v on vb.vehicle.id = v.id " +
			"group by vb.vehicle order by vcount DESC")
	List<Vehicle> selectByBookMarkTop3Vehicle(Pageable pageable);

	boolean existsByVehicle_IdAndEmployee_Id(Long vId, Long empId);
}