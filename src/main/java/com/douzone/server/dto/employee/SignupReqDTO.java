package com.douzone.server.dto.employee;


import com.douzone.server.entity.Department;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Position;
import com.douzone.server.entity.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

// of() dto -> entity
@Getter
@Setter
@NoArgsConstructor
public class SignupReqDTO {

	@NotNull(message = "팀 Id가 없습니다.")
	@Max(48)
	@Min(1)
	private long teamId;

	@Max(13)
	@Min(1)
	@NotNull(message = "직책 Id가 없습니다.")
	private long positionId;

	@Max(14)
	@Min(1)
	@NotNull(message = "부서 Id 없습니다.")
	private long deptId;

	private String empNo;

	@NotBlank(message = "비밀번호가 없습니다.")
	private String password;

	@NotBlank(message = "이름이 없습니다.")
	private String name;

	@NotBlank(message = "이메일이 없습니다.")
	@Email
	private String email;

	@NotBlank(message = "전화번호가 없습니다.")
	private String tel;

	@NotNull(message = "생년월일은 null 일 수 없습니다.")
	private LocalDateTime birthday;

	@Builder
	public SignupReqDTO(long teamId, long positionId, long deptId, String empNo, String password, String name, String email, String tel, LocalDateTime birthday) {
		this.teamId = teamId;
		this.positionId = positionId;
		this.deptId = deptId;
		this.empNo = empNo;
		this.password = password;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.birthday = birthday;
	}

	public Employee of(String empNo, String password) {
		return Employee.builder().empNo(empNo).password(password).name(name).email(email).tel(tel).birthday(birthday)
				.team(Team.builder().id((teamId)).department(Department.builder().id(deptId).build()).build())
				.position(Position.builder().id(positionId).build()).build();
	}
}
