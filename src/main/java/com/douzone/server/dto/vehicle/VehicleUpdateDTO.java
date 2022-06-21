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
@Builder
@AllArgsConstructor
public class VehicleUpdateDTO {

	@NotEmpty(message = "차량 이름은 필수 입력 사항입니다.")
	private String name;

	@NotEmpty(message = "차량 번호는 필수 입력 사항입니다.")
	private String number;

	@NotEmpty(message = "차량 종류는 필수 입력 사항입니다.")
	private String model;

	@NotEmpty(message = "차량 색상은 필수 입력 사항입니다.")
	private String color;

	@Min(2)
	@NotNull(message = "차량 탑승 가능 인원은 필수 입력 사항입니다.")
	private int capacity;
}
