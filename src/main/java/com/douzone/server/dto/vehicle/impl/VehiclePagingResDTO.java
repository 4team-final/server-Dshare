package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehiclePagingResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VehiclePagingResDTO implements IVehiclePagingResDTO {
	private Long reservationId;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime reservationCreatedAt;
	private LocalDateTime reservationModifiedAt;
	private String reason;
	private String title;
	private String vName;
	private String vNumber;
	private String color;
	private String model;
	private Integer capacity;
	private String vehicleImg;
	private String empNo;
	private String eName;
}
