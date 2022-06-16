package com.douzone.server.dto.reservation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SoonAndIngResDTO {

	private LocalDateTime soonRemainTime;
	private LocalDateTime ingRemainTime;

	@Builder
	public SoonAndIngResDTO(LocalDateTime soonRemainTime, LocalDateTime ingRemainTime) {
		this.soonRemainTime = soonRemainTime;
		this.ingRemainTime = ingRemainTime;
	}

	public SoonAndIngResDTO of(LocalDateTime soonTime, LocalDateTime remainTime) {
		return SoonAndIngResDTO.builder().soonRemainTime(soonTime).ingRemainTime(remainTime).build();
	}
}
