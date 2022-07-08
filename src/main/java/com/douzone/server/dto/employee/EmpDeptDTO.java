package com.douzone.server.dto.employee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class EmpDeptDTO {
	Long id;
	String name;

	public EmpDeptDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
