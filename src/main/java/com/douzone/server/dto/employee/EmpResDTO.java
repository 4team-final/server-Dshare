package com.douzone.server.dto.employee;

import com.douzone.server.dto.position.PositionResDTO;
import com.douzone.server.dto.team.TeamResDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmpResDTO {
	private Long id;
	private String empNo;
	private String name;
	private String tel;
	private String profileImg;
	private TeamResDTO team;
	private PositionResDTO position;
	
}
