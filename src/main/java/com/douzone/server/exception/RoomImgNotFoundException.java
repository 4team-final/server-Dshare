package com.douzone.server.exception;

public class RoomImgNotFoundException extends DshareServerException {
	public RoomImgNotFoundException(ErrorCode errorCode) {
		super(errorCode.ROOM_IMG_NOT_FOUND);
	}
}
