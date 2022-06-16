package com.douzone.server.dto.reservation;

import com.douzone.server.dto.employee.EmpResDTO;
import com.douzone.server.dto.room.RoomResDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RecentResDTO {
	// 예약
	private int id;
	private RoomResDTO room;
	private EmpResDTO emp;
	private String reason;
	private String title;
	private LocalDateTime rPeriod;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

}
