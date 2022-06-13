package com.douzone.server.employee.domain.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonTokenSet {
	private String accessToken;
	private ReIssuanceTokenSet reIssuanceTokenSet;
}
