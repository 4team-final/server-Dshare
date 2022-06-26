package com.douzone.server.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleSearchDTO {
	private Long vehicleId;
	private Integer capacity;
	private Long positionId;
	private Long teamId;
	private String empNo;
	private String startedAt;
	private String endedAt;
}
