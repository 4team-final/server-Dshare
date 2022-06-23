package com.douzone.server.dto.vehicle;

import java.time.LocalDateTime;

public interface IVehicleVariousDTO {
	Long getId();

	String getReason();

	String getTitle();

	String getVName();

	String getVNumber();

	String getModel();

	String getColor();

	String getVehicleImg();

	String getEmpNo();

	String getEName();

	String getEmail();

	String getTel();

	String getProfileImg();

	String getTeam();

	String getDept();

	String getPosition();

	LocalDateTime getStartedAt();

	LocalDateTime getEndedAt();

	LocalDateTime getCreatedAt();

	LocalDateTime getModifiedAt();

	LocalDateTime getBirthday();

	Integer getCapacity();
}
