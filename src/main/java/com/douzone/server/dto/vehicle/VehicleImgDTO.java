package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleImg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehicleImgDTO {
	private Long vehicleId;
	private String path;
	private String type;
	private String imgSize;

	public VehicleImg of(Long vehicleId) {
		return VehicleImg.builder()
				.vehicle(Vehicle.builder().id(vehicleId).build())
				.path(this.path)
				.type(this.type)
				.imgSize(this.imgSize)
				.build();
	}
}
