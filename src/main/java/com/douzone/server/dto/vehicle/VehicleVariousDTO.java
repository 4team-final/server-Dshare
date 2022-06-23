package com.douzone.server.dto.vehicle;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class VehicleVariousDTO implements IVehicleVariousDTO {
	private Long id;
	private String reason, title, vName, vNumber, model, color, vehicleImg, empNo, eName, email, tel, profileImg, team, dept, position;
	private LocalDateTime startedAt, endedAt, createdAt, modifiedAt, birthday;
	private Integer capacity;
}
