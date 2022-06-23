package com.douzone.server.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchDTO {
	private Long vehicleId;
	private Integer capacity;
	private Long positionId;
	private Long teamId;
	private Long deptId;
	private String empNo;
	private String startedAt;
	private String endedAt;
}
