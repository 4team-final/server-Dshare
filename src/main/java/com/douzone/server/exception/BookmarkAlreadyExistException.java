package com.douzone.server.exception;

public class BookmarkAlreadyExistException extends DshareServerException {
	public BookmarkAlreadyExistException(ErrorCode errorCode) {
		super(errorCode.BOOKMARK_ALREADY_EXIST);
	}
}
