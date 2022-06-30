package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleWeekDTO;
import com.douzone.server.entity.Vehicle;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWeekDTO implements IVehicleWeekDTO {
	private Long id;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String reason;
	private String title;
	private Vehicle vehicle;
	private int vcount;
	private List<String> imgList;

	public VehicleWeekDTO of(IVehicleWeekDTO i, List<String> list) {
		return VehicleWeekDTO.builder()
				.id(i.getId())
				.startedAt(i.getStartedAt())
				.endedAt(i.getEndedAt())
				.createdAt(i.getCreatedAt())
				.modifiedAt(i.getModifiedAt())
				.reason(i.getReason())
				.title(i.getTitle())
				.vehicle(i.getVehicle())
				.vcount(i.getVcount())
				.imgList(list)
				.build();
	}
}
