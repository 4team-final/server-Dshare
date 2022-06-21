package com.douzone.server.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
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
	private Integer deptId;
	private String empNo;
	private String empName;

	private String startedAt;
	private String endedAt;

//	@Builder
//	public RoomReservationSearchDTO(Integer roomNo, Integer capacity, String startedAt, String endedAt) {
//		this.roomNo = roomNo;
//		this.capacity = capacity;
//		this.startedAt = startedAt;
//		this.endedAt = endedAt;
//	}
	@Builder
	@QueryProjection
	public RoomReservationSearchDTO(Integer roomNo, Integer capacity, Long teamNo, Integer deptId, String empNo, String empName, String startedAt, String endedAt) {
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.teamNo = teamNo;
		this.deptId = deptId;
		this.empNo = empNo;
		this.empName = empName;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}
	@Builder
	@QueryProjection
	public RoomReservationSearchDTO(Long teamNo, Integer deptId, String empNo, String empName) {
		this.teamNo = teamNo;
		this.deptId = deptId;
		this.empNo = empNo;
		this.empName = empName;
	}
}
