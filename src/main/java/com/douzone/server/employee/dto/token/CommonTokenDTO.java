package com.douzone.server.employee.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonTokenDTO {
	private String accessToken;
	private ReIssuanceTokenDTO reIssuanceTokenDTO;
}
