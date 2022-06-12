package com.douzone.server.employee.domain.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReIssuanceTokenSet {
	private Long empNo;
	private String refreshToken;
}
