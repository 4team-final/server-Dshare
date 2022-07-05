package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleResDTO;
import com.douzone.server.entity.Vehicle;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResDTO implements IVehicleResDTO {
	private Long id;
	private String name;
	private String number;
	private String model;
	private String color;
	private int capacity;
	private List<String> imgList;

	public VehicleResDTO of(IVehicleResDTO i, List<String> list) {
		return VehicleResDTO.builder()
				.id(i.getId())
				.name(i.getName())
				.number(i.getNumber())
				.model(i.getModel())
				.color(i.getColor())
				.capacity(i.getCapacity())
				.imgList(list)
				.build();
	}

	public VehicleResDTO of(Vehicle vehicle, List<String> list) {
		return VehicleResDTO.builder()
				.id(vehicle.getId())
				.name(vehicle.getName())
				.model(vehicle.getModel())
				.color(vehicle.getColor())
				.number(vehicle.getNumber())
				.capacity(vehicle.getCapacity())
				.imgList(list)
				.build();
	}
}
