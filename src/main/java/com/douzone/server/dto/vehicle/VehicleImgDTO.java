package com.douzone.server.dto.vehicle;

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
}
