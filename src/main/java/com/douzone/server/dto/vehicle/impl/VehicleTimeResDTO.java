package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.IVehicleTimeResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class VehicleTimeResDTO implements IVehicleTimeResDTO {
	private Long id;
	private String title;
	private LocalDateTime dateTime;
}
