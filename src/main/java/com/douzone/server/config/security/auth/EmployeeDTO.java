package com.douzone.server.config.security.auth;

import com.douzone.server.dto.position.PositionResDTO;
import com.douzone.server.dto.team.TeamResDTO;
import com.douzone.server.entity.Employee;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class EmployeeDTO {
	private Long id;
	private String empNo;
	private String password;
	private String name;
	private String email;
	private String tel;
	private LocalDateTime birthday;
	private String profileImg;
	private String role;
	private TeamResDTO team;
	private PositionResDTO position;

	@Builder
	public EmployeeDTO(Long id, String empNo, String password, String name, String email, String tel, LocalDateTime birthday, String profileImg, String role, TeamResDTO team, PositionResDTO position) {
		this.id = id;
		this.empNo = empNo;
		this.password = password;
		this.name = name;
		this.email = email;
		this.tel = tel;
		this.birthday = birthday;
		this.profileImg = profileImg;
		this.role = role;
		this.team = team;
		this.position = position;
	}

	public EmployeeDTO of(Employee employee) {
		return EmployeeDTO.builder()
				.id(employee.getId())
				.empNo(employee.getEmpNo())
				.password(employee.getPassword())
				.name(employee.getPassword())
				.email(employee.getEmail())
				.tel(employee.getTel())
				.birthday(employee.getBirthday())
				.profileImg(employee.getProfileImg())
				.role(employee.getRole())
				.team(TeamResDTO.builder().build().of(employee.getTeam()))
				.position(PositionResDTO.builder().build().of(employee.getPosition()))
				.build();
	}
}
