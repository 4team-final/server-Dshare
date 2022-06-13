package com.douzone.server.employee.dto.token;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResDTO {
	private int code;
	private String token;
}
