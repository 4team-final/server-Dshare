package com.douzone.server.dto.team;

import com.douzone.server.dto.department.DepartmentResDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeamResDTO {
	private int teamId;
	private DepartmentResDTO department;
	private int teamName;
}
