package com.douzone.server.config.socket.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TimeVehicleRepository extends JpaRepository<TimeVehicle, Long> {
	List<TimeVehicle> findByCalendar_Uid(String uid);

	@Query("select tv from TimeVehicle tv where tv.calendar.uid = :uid and tv.vehicleId = :vid")
	Optional<List<TimeVehicle>> selectByUidAndVid(@Param("uid") String uid, @Param("vid") Long vehicleId);
}
