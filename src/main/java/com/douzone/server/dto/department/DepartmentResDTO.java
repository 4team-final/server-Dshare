package com.douzone.server.dto.department;

import com.douzone.server.entity.Department;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentResDTO {
	private long deptId;
	private String deptName;

	@Builder
	public DepartmentResDTO(long deptId, String deptName) {
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public DepartmentResDTO of(Department department) {
		return DepartmentResDTO.builder()
				.deptId(department.getId())
				.deptName(department.getName())
				.build();
	}
}
