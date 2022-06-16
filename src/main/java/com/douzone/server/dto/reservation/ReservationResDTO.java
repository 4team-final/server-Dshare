package com.douzone.server.dto.reservation;

import com.douzone.server.dto.employee.EmpResDTO;
import com.douzone.server.dto.room.RoomResDTO;
import com.douzone.server.entity.RoomReservation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReservationResDTO {
	// 예약
	private long id;
	private RoomResDTO room;
	private EmpResDTO emp;
	private String reason;
	private String title;
	private LocalDateTime rPeriod;
	private LocalDateTime startedAt;
	private LocalDateTime endedAt;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	@Builder
	public ReservationResDTO(long id, RoomResDTO room, EmpResDTO emp, String reason, String title, LocalDateTime rPeriod, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.id = id;
		this.room = room;
		this.emp = emp;
		this.reason = reason;
		this.title = title;
		this.rPeriod = rPeriod;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}

	public ReservationResDTO of(RoomReservation reservation, LocalDateTime rPeriod) {
		return ReservationResDTO.builder()
				.id(reservation.getId())
				.room(RoomResDTO.builder().build().of(reservation.getMeetingRoom()))
				.emp(EmpResDTO.builder().build().of(reservation.getEmployee()))
				.reason(reservation.getReason())
				.title(reservation.getTitle())
				.rPeriod(rPeriod)
				.startedAt(reservation.getStartedAt())
				.endedAt(reservation.getEndedAt())
				.createdAt(reservation.getCreatedAt())
				.modifiedAt(reservation.getModifiedAt())
				.build();
	}
}
