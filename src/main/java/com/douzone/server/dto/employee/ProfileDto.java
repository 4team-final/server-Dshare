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
@Builder
public class ProfileDto {

	private String empNo;
	private String name;
	private String email;
	private String tel;
	private String position;
	private String team;
	private String dept;
	private LocalDateTime birthday;
	private String profileImg;
	private String role;

	public void updateProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	public ProfileDto(String empNo, String name, String email, String tel, String position, String team, String dept, LocalDateTime birthday, String profileImg, String role) {
		this.empNo = empNo;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.position = position;
		this.team = team;
		this.dept = dept;
		this.birthday = birthday;
		this.profileImg = profileImg;
		this.role = role;
	}

	public ProfileDto of(Employee employee) {
		return ProfileDto.builder()
				.empNo(employee.getEmpNo())
				.name(employee.getName())
				.email(employee.getEmail())
				.tel(employee.getTel())
				.birthday(employee.getBirthday())
				.profileImg(employee.getProfileImg())
				.position(employee.getPosition().getName())
				.team(employee.getTeam().getName())
				.dept(employee.getTeam().getDepartment().getName())
				.build();
	}

}
