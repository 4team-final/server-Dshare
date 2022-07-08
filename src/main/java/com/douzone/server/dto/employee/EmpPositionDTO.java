package com.douzone.server.dto.employee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class EmpPositionDTO {
	Long id;
	String name;

	public EmpPositionDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
