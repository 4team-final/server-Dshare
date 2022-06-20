package com.douzone.server.dto.vehicle;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReservationDTO {

	@Min(1)
	@NotNull(message = "차량은 필수 선택 항목입니다.")
	private int vehicleId;

	private int empId;

	@NotEmpty(message = "사유은 필수 작성 항목입니다.")
	private String reason;

	@NotEmpty(message = "제목은 필수 작성 항목입니다.")
	private String title;

	@NotNull(message = "예약 시작 시간은 필수 작성 항목입니다.")
	private LocalDateTime startedAt;

	@NotNull(message = "예약 종료 시간은 필수 작성 항목입니다.")
	private LocalDateTime endedAt;
}
