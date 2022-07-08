package com.douzone.server.dto.employee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpTeamDTO {

	Long id;
	Long deptId;
	String name;
	@Builder
	public EmpTeamDTO(Long id, Long deptId, String name) {
		this.id = id;
		this.deptId = deptId;
		this.name = name;
	}
}
