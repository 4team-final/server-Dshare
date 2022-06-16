package com.douzone.server.dto.team;

import com.douzone.server.dto.department.DepartmentResDTO;
import com.douzone.server.entity.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamResDTO {
	private long teamId;
	private DepartmentResDTO department;
	private String teamName;

	@Builder
	public TeamResDTO(long teamId, DepartmentResDTO department, String teamName) {
		this.teamId = teamId;
		this.department = department;
		this.teamName = teamName;
	}

	public TeamResDTO of(Team team) {
		return TeamResDTO.builder().teamId(team.getId()).department(DepartmentResDTO.builder().build().of(team.getDept())).teamName(team.getName()).build();
	}
}
