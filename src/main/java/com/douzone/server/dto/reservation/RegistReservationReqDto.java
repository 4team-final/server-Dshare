package com.douzone.server.dto.reservation;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class RegistReservationReqDto {

	@NotNull
	private long empId;
	@NotNull
	private long roomId;
	@NotEmpty
	private String reason;
	@NotEmpty
	private String title;
	@NotNull
	private LocalDateTime startedAt;
	@NotNull
	private LocalDateTime endedAt;


}
