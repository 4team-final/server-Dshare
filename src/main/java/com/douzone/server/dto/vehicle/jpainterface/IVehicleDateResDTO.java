package com.douzone.server.dto.vehicle.jpainterface;

import com.douzone.server.entity.Vehicle;

import java.time.LocalDateTime;

public interface IVehicleDateResDTO {
	LocalDateTime getEndedAt();

	LocalDateTime getModifiedAt();

	Vehicle getVehicle();
}
