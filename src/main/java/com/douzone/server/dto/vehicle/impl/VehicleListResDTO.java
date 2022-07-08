package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleListResDTO;
import com.douzone.server.entity.Vehicle;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleListResDTO implements IVehicleListResDTO {
	private Long id;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String reason;
	private String title;
	private Vehicle vehicle;
	private List<String> imgList;
	private String empNo;
	private String name;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer month;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer day;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer count;


	public VehicleListResDTO of(IVehicleListResDTO i, List<String> list) {
		return VehicleListResDTO.builder()
				.id(i.getId())
				.startedAt(i.getStartedAt())
				.endedAt(i.getEndedAt())
				.createdAt(i.getCreatedAt())
				.modifiedAt(i.getModifiedAt())
				.reason(i.getReason())
				.title(i.getTitle())
				.vehicle(i.getVehicle())
				.imgList(list)
				.empNo(i.getEmpNo())
				.name(i.getName())
				.build();
	}

	public VehicleListResDTO of2(IVehicleListResDTO i, List<String> list) {
		return VehicleListResDTO.builder()
				.id(i.getId())
				.startedAt(i.getStartedAt())
				.endedAt(i.getEndedAt())
				.createdAt(i.getCreatedAt())
				.modifiedAt(i.getModifiedAt())
				.reason(i.getReason())
				.title(i.getTitle())
				.vehicle(i.getVehicle())
				.imgList(list)
				.empNo(i.getEmpNo())
				.name(i.getName())
				.count(i.getCount())
				.day(i.getDay())
				.month(i.getMonth())
				.build();
	}
}
