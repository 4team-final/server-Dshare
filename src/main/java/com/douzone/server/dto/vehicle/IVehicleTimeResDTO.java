package com.douzone.server.dto.vehicle;

import java.time.LocalDateTime;

public interface IVehicleTimeResDTO {
	Long getId();

	String getTitle();

	LocalDateTime getDateTime();
}
