package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Vehicle;

import java.time.LocalDateTime;

public interface IVehicleDateResDTO {
	LocalDateTime getEndedAt();

	Vehicle getVehicle();

	String getVehicleImg();
}
