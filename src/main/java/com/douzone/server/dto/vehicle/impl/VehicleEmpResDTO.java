package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleEmpResDTO;
import com.douzone.server.entity.Vehicle;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEmpResDTO implements IVehicleEmpResDTO {
	private Long id;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String reason;
	private String title;
	private Vehicle vehicle;
	private List<String> impList;

	public VehicleEmpResDTO of(IVehicleEmpResDTO i, List<String> list) {
		return VehicleEmpResDTO.builder()
				.id(i.getId())
				.startedAt(i.getStartedAt())
				.endedAt(i.getEndedAt())
				.createdAt(i.getCreatedAt())
				.modifiedAt(i.getModifiedAt())
				.reason(i.getReason())
				.title(i.getTitle())
				.vehicle(i.getVehicle())
				.impList(list)
				.build();
	}
}
