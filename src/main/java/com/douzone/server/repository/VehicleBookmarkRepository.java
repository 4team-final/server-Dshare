package com.douzone.server.repository;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleBookmarkDTO;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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


	@Query("select vb from VehicleBookmark vb " +
			"join fetch Vehicle v on vb.vehicle.id = v.id " +
			"join fetch Employee e on vb.employee.id = e.id " +
			"where vb.employee.id = :empId and vb.vehicle.id = :vId")
	List<IVehicleBookmarkDTO> findByThisBookMarkVehicle(@Param("empId") Long empId, @Param("vId") Long vId);

	void deleteByEmployee_IdAndVehicle_Id(Long empId, Long vId);
}