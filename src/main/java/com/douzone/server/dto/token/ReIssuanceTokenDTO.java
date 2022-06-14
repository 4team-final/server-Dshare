package com.douzone.server.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReIssuanceTokenDTO {
	private String empNo;
	private String refreshToken;
}
