package com.douzone.server.employee.domain.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReIssuanceTokenSet {
	private String empNo;
	private String refreshToken;
}
