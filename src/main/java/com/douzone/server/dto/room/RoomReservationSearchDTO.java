package com.douzone.server.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomReservationSearchDTO {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer roomNo;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer capacity;
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
