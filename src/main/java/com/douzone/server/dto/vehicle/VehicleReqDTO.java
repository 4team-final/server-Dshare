package com.douzone.server.dto.vehicle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VehicleReqDTO {
	private Long id;
	private Long vehicleId;
	private String reason;
	private String title;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
}
