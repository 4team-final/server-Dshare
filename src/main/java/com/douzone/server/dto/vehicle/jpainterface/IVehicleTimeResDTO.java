package com.douzone.server.dto.vehicle.jpainterface;

import java.time.LocalDateTime;

public interface IVehicleTimeResDTO {
	Long getId();

	String getTitle();

	LocalDateTime getTimeTime();
}
