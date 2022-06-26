package com.douzone.server.repository;

import com.douzone.server.entity.VehicleImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleImgRepository extends JpaRepository<VehicleImg, Long> {
	Optional<VehicleImg> findByVehicleId(Long id);

	void deleteByVehicleId(Long id);
}
