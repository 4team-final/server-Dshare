package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleDateResDTO;
import com.douzone.server.entity.Vehicle;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDateResDTO implements IVehicleDateResDTO {
	private LocalDateTime endedAt;
	private LocalDateTime modifiedAt;
	private Vehicle vehicle;
	private List<String> imgList;

	public VehicleDateResDTO of(IVehicleDateResDTO i, List<String> list) {
		return VehicleDateResDTO.builder()
				.modifiedAt(i.getModifiedAt())
				.endedAt(i.getEndedAt())
				.vehicle(i.getVehicle())
				.imgList(list)
				.build();
	}
}
