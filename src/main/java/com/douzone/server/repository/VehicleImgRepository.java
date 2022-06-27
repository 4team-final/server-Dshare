package com.douzone.server.repository;

import com.douzone.server.entity.VehicleImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleImgRepository extends JpaRepository<VehicleImg, Long> {
	List<VehicleImg> findByVehicle_Id(Long id);

	@Query("select vi.path from VehicleImg vi where vi.vehicle.id = :id")
	List<String> findPathByVehicleId(@Param("id") Long id);
}
