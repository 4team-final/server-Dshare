package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleWeekTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWeekTimeDTO implements IVehicleWeekTimeDTO {
	private Integer hTime;
	private Integer hCount;
	private Long vId;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private String reason;
	private String title;
	private LocalDateTime cre;
	private LocalDateTime mmd;
	private String vehicleImg;
	private String model;
	private String color;
	private String vNum;
	private String vNam;
	private Integer capacity;
	private String empNo;
	private String eName;
}
