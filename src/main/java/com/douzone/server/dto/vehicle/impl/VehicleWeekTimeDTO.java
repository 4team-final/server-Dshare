package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleWeekTimeDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWeekTimeDTO implements IVehicleWeekTimeDTO {
	private Integer hTime;
	private Integer hCount;
	private Long reservationId;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private String reason;
	private String title;
	private LocalDateTime reservationCreatedAt;
	private LocalDateTime reservationModifiedAt;
	private Long vId;
	private String model;
	private String color;
	private String vNumber;
	private String vName;
	private Integer capacity;
	private String empNo;
	private String eName;
	public List<String> imgList;

	public VehicleWeekTimeDTO of(IVehicleWeekTimeDTO i, List<String> list) {
		return VehicleWeekTimeDTO.builder()
				.hTime(i.getHTime())
				.hCount(i.getHCount())
				.reservationId(i.getReservationId())
				.startedAt(i.getStartedAt())
				.endedAt(i.getEndedAt())
				.reason(i.getReason())
				.title(i.getTitle())
				.reservationCreatedAt(i.getReservationCreatedAt())
				.reservationModifiedAt(i.getReservationModifiedAt())
				.vId(i.getVId())
				.model(i.getModel())
				.color(i.getColor())
				.vNumber(i.getVNumber())
				.vName(i.getVName())
				.capacity(i.getCapacity())
				.empNo(i.getEmpNo())
				.eName(i.getEName())
				.imgList(list)
				.build();
	}
}
