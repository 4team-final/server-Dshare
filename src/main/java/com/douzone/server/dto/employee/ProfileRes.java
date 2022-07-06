package com.douzone.server.dto.employee;


import com.douzone.server.entity.Employee;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class ProfileRes {
	private Long id;
	private String empNo;
	private String name;
	private String email;
	private String tel;
	private String position;
	private Long positionId;
	private String team;
	private Long teamId;
	private String dept;
	private Long deptId;
	private LocalDateTime birthday;
	private String profileImg;
	private String role;

	public void updateProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public ProfileRes(Long id, String empNo, String name, String email, String tel, String position, Long positionId, String team, Long teamId, String dept, Long deptId, LocalDateTime birthday, String profileImg, String role) {
		this.id = id;
		this.empNo = empNo;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.position = position;
		this.positionId = positionId;
		this.team = team;
		this.teamId = teamId;
		this.dept = dept;
		this.deptId = deptId;
		this.birthday = birthday;
		this.profileImg = profileImg;
		this.role = role;
	}

	public ProfileRes of(Employee employee, Long empId) {
		return ProfileRes.builder()
				.id(empId)
				.empNo(employee.getEmpNo())
				.name(employee.getName())
				.email(employee.getEmail())
				.tel(employee.getTel())
				.birthday(employee.getBirthday())
				.profileImg(employee.getProfileImg())
				.position(employee.getPosition().getName())
				.team(employee.getTeam().getName())
				.dept(employee.getTeam().getDepartment().getName())
				.teamId(employee.getTeam().getId())
				.deptId(employee.getTeam().getDepartment().getId())
				.positionId(employee.getPosition().getId())
				.build();
	}

}
