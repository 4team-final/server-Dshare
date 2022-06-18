package com.douzone.server.exception;

public class RoomNotFoundException extends DshareServerException {
	public RoomNotFoundException(ErrorCode errorCode) {
		super(errorCode.EMP_NOT_FOUND);
	}
}
