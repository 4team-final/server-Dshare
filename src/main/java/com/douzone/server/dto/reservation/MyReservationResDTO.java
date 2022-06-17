package com.douzone.server.dto.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyReservationResDTO {
	private List<ReservationResDTO> beforeList;
	private List<ReservationResDTO> afterList;

	@Builder
	public MyReservationResDTO(List<ReservationResDTO> beforeList, List<ReservationResDTO> afterList) {
		this.beforeList = beforeList;
		this.afterList = afterList;
	}

	public MyReservationResDTO of(List<ReservationResDTO> beforeList, List<ReservationResDTO> afterList) {
		return MyReservationResDTO.builder()
				.beforeList(beforeList)
				.afterList(afterList)
				.build();
	}
}
