package com.douzone.server.dto.reservation;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PropertySource("classpath:messages.properties")
@Builder
public class RegistReservationReqDto {

	@NotNull(groups = registRes.class)
	private Long empId;
	@NotNull(groups = {registRes.class, updateRes.class})
	private Long roomId;

	@NotBlank(groups = {registRes.class, updateRes.class})
	private String reason;
	@NotBlank(groups = {registRes.class, updateRes.class})
	private String title;
	@NotNull(groups = {registRes.class, updateRes.class}) @FutureOrPresent
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime startedAt;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@NotNull(groups = {registRes.class, updateRes.class}) @FutureOrPresent
	private LocalDateTime endedAt;




}
