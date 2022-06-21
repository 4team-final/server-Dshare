package com.douzone.server.dto.reservation;

import com.douzone.server.dto.employee.EmpResDTO;
import com.douzone.server.dto.room.RoomImgResDTO;
import com.douzone.server.dto.room.RoomObjectResDTO;
import com.douzone.server.dto.room.RoomResDTO;
import com.douzone.server.entity.RoomReservation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReservationPagingRes {
	// 예약
	private ReservationResDTO reservationResDTO;
	private long total;
	private int limit;
	private long lastId;



	@Builder
	public ReservationPagingRes(ReservationResDTO reservationResDTO, long total, int limit, long lastId) {
		this.reservationResDTO = reservationResDTO;
		this.total = total;
		this.limit = limit;
		this.lastId = lastId;
	}


	public ReservationPagingRes of(ReservationResDTO reservationResDTO, long total, int limit , long lastId) {
		return ReservationPagingRes.builder().reservationResDTO(reservationResDTO).total(total).limit(limit).lastId(lastId).build();
	}

}
