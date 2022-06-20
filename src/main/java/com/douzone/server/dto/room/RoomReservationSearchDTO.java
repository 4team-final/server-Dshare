package com.douzone.server.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomReservationSearchDTO {

	private Integer roomNo;
	private Integer capacity;
	private Long teamNo;
	private Long DeptNo;
	private String empNo;
	private String empName;

	private String startedAt;
	private String endedAt;

	@Builder
	public RoomReservationSearchDTO(Integer roomNo, Integer capacity, String startedAt, String endedAt) {
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}
}
