package com.douzone.server.dto.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SoonAndIngResDTO {

	private Long soonRemainTime;
	private Long ingRemainTime;

	@Builder
	public SoonAndIngResDTO(Long soonRemainTime, Long ingRemainTime) {
		this.soonRemainTime = soonRemainTime;
		this.ingRemainTime = ingRemainTime;
	}


	public SoonAndIngResDTO of(Long soonTime, Long remainTime) {
		return SoonAndIngResDTO.builder().soonRemainTime(soonTime).ingRemainTime(remainTime).build();
	}
}
