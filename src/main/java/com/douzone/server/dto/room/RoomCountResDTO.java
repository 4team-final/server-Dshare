package com.douzone.server.dto.room;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import com.douzone.server.entity.RoomReservation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter

@NoArgsConstructor
public class RoomCountResDTO {

	private Long id;

	private Integer roomNo;
	private Integer month;
	private Integer day;
	private long count ;

	private String reason;
	private String title;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;

	@Builder
	@QueryProjection
	public RoomCountResDTO(Long id, Integer roomNo, Integer month, Integer day, long count, String reason, String title, LocalDateTime startedAt, LocalDateTime endedAt) {
		this.id = id;
		this.roomNo = roomNo;
		this.month = month;
		this.day = day;
		this.count = count;
		this.reason = reason;
		this.title = title;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}
}
