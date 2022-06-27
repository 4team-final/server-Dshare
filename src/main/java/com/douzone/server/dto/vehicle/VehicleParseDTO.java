package com.douzone.server.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VehicleParseDTO {

	private Long id;
	private int empId;

	@Min(1)
	@NotNull(message = "차량은 필수 선택 항목입니다.")
	private Long vehicleId;

	@NotEmpty(message = "사유는 필수 입력 사항입니다.")
	private String reason;

	@NotEmpty(message = "제목은 필수 입력 사항입니다.")
	private String title;

	@NotEmpty(message = "시작 시간은 필수 입력 사항입니다.")
	private String startedAt;

	@NotEmpty(message = "종료 시간은 필수 입력 사항입니다.")
	private String endedAt;
}
