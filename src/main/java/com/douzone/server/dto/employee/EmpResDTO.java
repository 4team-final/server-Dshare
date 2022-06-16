package com.douzone.server.dto.employee;

import com.douzone.server.dto.position.PositionResDTO;
import com.douzone.server.dto.team.TeamResDTO;
import com.douzone.server.entity.Employee;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpResDTO {
	private Long empId;
	private String empNo;
	private String name;
	private String tel;
	private String profileImg;
	private TeamResDTO team;
	private PositionResDTO position;

	@Builder
	public EmpResDTO(Long empId, String empNo, String name, String tel, String profileImg, TeamResDTO team, PositionResDTO position) {
		this.empId = empId;
		this.empNo = empNo;
		this.name = name;
		this.tel = tel;
		this.profileImg = profileImg;
		this.team = team;
		this.position = position;
	}

	public EmpResDTO of(Employee employee) {
		return EmpResDTO.builder().empId(employee.getId()).empNo(employee.getEmpNo()).name(employee.getName()).tel(employee.getTel()).profileImg(employee.getProfileImg()).team(TeamResDTO.builder().build().of(employee.getTeam())).position(PositionResDTO.builder().build().of(employee.getPosition())).build();
	}
}
