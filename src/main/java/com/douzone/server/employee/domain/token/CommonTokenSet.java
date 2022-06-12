package com.douzone.server.employee.domain.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CommonTokenSet {
	private String accessToken;
	private ReIssuanceTokenSet reIssuanceTokenSet;
}
