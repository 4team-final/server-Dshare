package com.douzone.server.config.socket.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimeVehicleRepository extends JpaRepository<TimeVehicle, Long> {
	Optional<List<TimeVehicle>> findByCalendar_Uid(String uid);
}
