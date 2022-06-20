package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.IVehicleResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResDTO implements IVehicleResDTO {
	private Long id;
	private String name;
	private String number;
	private String model;
	private String color;
	private int capacity;
	private String vehicleImg;
}
