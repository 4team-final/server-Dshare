package com.douzone.server.dto.employee;


import com.douzone.server.entity.Department;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Position;
import com.douzone.server.entity.Team;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SignModReqDTO {

	@NotNull(message = "팀 Id가 없습니다.", groups = {mod.class, signUp.class})
	@Max(48)
	@Min(1)
	private long teamId;

	@Max(13)
	@Min(1)
	@NotNull(message = "직책 Id가 없습니다.", groups = {mod.class, signUp.class})
	private long positionId;

	@Max(14)
	@Min(1)
	@NotNull(message = "부서 Id 없습니다.", groups = {mod.class, signUp.class})
	private long deptId;

	private String empNo;

	@NotBlank(message = "비밀번호가 없습니다.", groups = signUp.class)
	private String password;

	@NotBlank(groups = {modPw.class}) //비밀번호 변경시 검증용입니다.
	private String originPassword;

	@NotBlank(message = "이름이 없습니다.", groups = {mod.class, signUp.class})
	private String name;

	@NotBlank(message = "이메일이 없습니다.", groups = {mod.class, signUp.class})
	@Email
	private String email;

	@NotBlank(message = "전화번호가 없습니다.", groups = {mod.class, signUp.class})
	private String tel;

	@NotNull(message = "생년월일은 null 일 수 없습니다.", groups = {mod.class, signUp.class})
	private LocalDateTime birthday;

	@Builder
	public SignModReqDTO(long teamId, long positionId, long deptId, String empNo, String password, String originPassword, String name, String email, String tel, LocalDateTime birthday) {
		this.teamId = teamId;
		this.positionId = positionId;
		this.deptId = deptId;
		this.empNo = empNo;
		this.password = password;
		this.originPassword = originPassword;
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

	public StringBuilder makeEmpno(Employee employee, String year) {
		String deptId = String.format("%02d", this.getDeptId());
		String empId = String.format("%05d", employee.getId() + 1);
		return new StringBuilder().append(year).append(deptId).append(empId);

	}
}
