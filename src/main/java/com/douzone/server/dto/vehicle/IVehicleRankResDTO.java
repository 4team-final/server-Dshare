package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Vehicle;

import java.time.LocalDateTime;

public interface IVehicleRankResDTO {
	Long getId();

	LocalDateTime getStartedAt();

	LocalDateTime getEndedAt();

	LocalDateTime getCreatedAt();

	LocalDateTime getModifiedAt();

	String getReason();

	String getTitle();

	Vehicle getVehicle();

	int getVcount();
}
