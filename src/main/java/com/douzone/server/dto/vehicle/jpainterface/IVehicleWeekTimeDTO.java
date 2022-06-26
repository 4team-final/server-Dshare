package com.douzone.server.dto.vehicle.jpainterface;

import java.time.LocalDateTime;

public interface IVehicleWeekTimeDTO {
	Integer getHTime();

	Integer getHCount();

	Long getVId();

	LocalDateTime getStartedAt();

	LocalDateTime getEndedAt();

	LocalDateTime getCre();

	LocalDateTime getMmd();

	String getReason();

	String getTitle();

	String getVehicleImg();

	String getModel();

	String getColor();

	String getVNum();

	String getVNam();

	Integer getCapacity();

	String getEmpNo();

	String getEName();
}
