package com.douzone.server.exception;

public class RoomObjectNotFoundException extends DshareServerException {

	public RoomObjectNotFoundException(ErrorCode errorCode) {
		super(errorCode.ROOM_OBJECT_NOT_FOUND);
	}
}
