package com.douzone.server.employee.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReIssuanceTokenDTO {
	private String empNo;
	private String refreshToken;
}
