package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBookMarkDTO {
	private Long id;
	private String name;
	private String model;
	private String color;
	private String number;
	private int capacity;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private List<String> imgList;

	public VehicleBookMarkDTO of(Vehicle vehicle, List<String> list) {
		return VehicleBookMarkDTO.builder()
				.id(vehicle.getId())
				.name(vehicle.getName())
				.model(vehicle.getModel())
				.color(vehicle.getColor())
				.number(vehicle.getNumber())
				.capacity(vehicle.getCapacity())
				.createdAt(vehicle.getCreatedAt())
				.modifiedAt(vehicle.getModifiedAt())
				.imgList(list)
				.build();
	}
}
