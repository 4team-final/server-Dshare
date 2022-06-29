package com.douzone.server.exception;

public class TokenSetNotFoundException extends DshareServerException {
	public TokenSetNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
