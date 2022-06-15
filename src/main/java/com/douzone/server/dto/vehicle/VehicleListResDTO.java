package com.douzone.server.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleListResDTO {
	private Long id;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private String reason;
	private String title;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String empNo;
	private String name;
}
