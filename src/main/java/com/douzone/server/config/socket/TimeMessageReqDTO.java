package com.douzone.server.config.socket;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@JsonInclude
public class TimeMessageReqDTO {

	public enum MessageType {
		ENTER, TALK, QUIT
	}

	private MessageType type;
	private String uid;
	private Integer[] time = new Integer[18];
	private String empNo;

}
