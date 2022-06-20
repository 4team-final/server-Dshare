package com.douzone.server.dto.reservation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeekCountHourResDTO {

	private Long roomId;
	private String content;
	private String categoryName;
	private Integer roomNo;
	private Integer capacity;
	private LocalDateTime modifiedAt;

	private Long count;
	private Integer hour;

	private List<ReservationResDTO> reservationResDTOList;

	@Builder
	@QueryProjection
	public WeekCountHourResDTO(Long roomId, String content, String categoryName, Integer roomNo, Integer capacity, LocalDateTime modifiedAt, Long count, Integer hour) {
		this.roomId = roomId;
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.modifiedAt = modifiedAt;
		this.count = count;
		this.hour = hour;
	}

	@Builder
	@QueryProjection
	public WeekCountHourResDTO(Long roomId, String content, String categoryName, Integer roomNo, Integer capacity, LocalDateTime modifiedAt, Long count) {
		this.roomId = roomId;
		this.content = content;
		this.categoryName = categoryName;
		this.roomNo = roomNo;
		this.capacity = capacity;
		this.modifiedAt = modifiedAt;
		this.count = count;

	}
}
