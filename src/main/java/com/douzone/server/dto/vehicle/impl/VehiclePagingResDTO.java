package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehiclePagingResDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePagingResDTO implements IVehiclePagingResDTO {
	private Long reservationId;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime reservationCreatedAt;
	private LocalDateTime reservationModifiedAt;
	private String reason;
	private String title;
	private String vName;
	private String vNumber;
	private String color;
	private String model;
	private Integer capacity;
	private String empNo;
	private String eName;
	private Long vId;
	private List<String> imgList;

	public VehiclePagingResDTO of(IVehiclePagingResDTO i, List<String> list) {
		return VehiclePagingResDTO.builder()
				.reservationId(i.getReservationId())
				.startedAt(i.getStartedAt())
				.endedAt(i.getEndedAt())
				.reservationCreatedAt(i.getReservationCreatedAt())
				.reservationModifiedAt(i.getReservationModifiedAt())
				.reason(i.getReason())
				.title(i.getTitle())
				.vName(i.getVName())
				.vNumber(i.getVNumber())
				.color(i.getColor())
				.model(i.getModel())
				.capacity(i.getCapacity())
				.empNo(i.getEmpNo())
				.eName(i.getEName())
				.vId(i.getVId())
				.imgList(list)
				.build();
	}
}
