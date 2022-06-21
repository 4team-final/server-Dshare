package com.douzone.server.dto.vehicle;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReservationDTO {
	private Long vehicleId;
	private int empId;
	private String reason;
	private String title;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
}
