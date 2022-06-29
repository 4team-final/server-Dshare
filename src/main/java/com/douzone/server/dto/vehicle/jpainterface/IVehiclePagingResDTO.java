package com.douzone.server.dto.vehicle.jpainterface;

import java.time.LocalDateTime;

public interface IVehiclePagingResDTO {
	Long getReservationId();

	LocalDateTime getStartedAt();

	LocalDateTime getEndedAt();

	LocalDateTime getReservationCreatedAt();

	LocalDateTime getReservationModifiedAt();

	String getReason();

	String getTitle();

	String getVName();

	String getVNumber();

	String getColor();

	String getModel();

	Integer getCapacity();

	String getEmpNo();

	String getEName();

	Long getVId();

}
