package com.douzone.server.dto.vehicle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VehicleReqDTO {
	@Min(1)
	@NotNull(message = "예약 번호는 null 일 수 없습니다.")
	private Long id;

	@Min(1)
	@NotNull(message = "차량 번호는 필수 입력 사항입니다.")
	private Long vehicleId;

	@NotEmpty(message = "사유는 필수 입력 사항입니다.")
	private String reason;

	@NotEmpty(message = "제목은 필수 입력 사항입니다.")
	private String title;

	@NotNull(message = "시작 시간은 필수 입력 사항입니다.")
	private LocalDateTime startedAt;

	@NotNull(message = "종료 시간은 필수 입력 사항입니다.")
	private LocalDateTime endedAt;
}
