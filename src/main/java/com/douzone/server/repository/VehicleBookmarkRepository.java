package com.douzone.server.repository;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VehicleBookmarkRepository extends JpaRepository<VehicleBookmark, Long> {

    @Query("select v from VehicleBookmark vb left join fetch Vehicle v on vb.vehicle.id = v.id where vb.employee.empNo = :empNo")
    List<Vehicle> findMarkVehicle(@Param("empNo") String empNo);

    @Query("select v, count(vb) from VehicleBookmark vb left join fetch Vehicle v on vb.vehicle.id = v.id group by vb.vehicle order by count(vb) DESC")
    List<Vehicle> findMarkBest(Pageable pageable);
}