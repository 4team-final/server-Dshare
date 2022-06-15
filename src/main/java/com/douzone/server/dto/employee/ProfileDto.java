package com.douzone.server.dto.employee;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

	private String empNo;
	private String name;
	private String email;
	private String tel;
	private LocalDateTime birthday;
	private String profileImg;
	private Position position;

}
