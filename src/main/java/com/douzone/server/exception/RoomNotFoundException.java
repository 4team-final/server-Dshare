package com.douzone.server.exception;

public class RoomNotFoundException extends DshareServerException {
	public RoomNotFoundException(ErrorCode errorCode) {
		super(errorCode.ROOM_NOT_FOUND);
	}
}
