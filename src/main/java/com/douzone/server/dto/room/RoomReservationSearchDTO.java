package com.douzone.server.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomReservationSearchDTO {

	private Integer roomNo;
	private Integer capacity;

	private Long positionId;
	private Long teamId;
	private Long deptId;
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
	public RoomReservationSearchDTO(Integer roomNo, Integer capacity, Long positionId, Long teamId, Long deptId, String empNo, String empName, String startedAt, String endedAt) {
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.positionId = positionId;
		this.teamId = teamId;
		this.deptId = deptId;
		this.empNo = empNo;
		this.empName = empName;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}

	@Builder
	@QueryProjection
	public RoomReservationSearchDTO(Long positionId, Long teamId, Long deptId, String empNo, String empName) {
		this.positionId = positionId;
		this.teamId = teamId;
		this.deptId = deptId;
		this.empNo = empNo;
		this.empName = empName;
	}
}
