package com.douzone.server.config.socket.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleSocketReqDTO {
	public enum MessageType {
		ENTER, TALK, QUIT
	}

	private VehicleSocketReqDTO.MessageType type;
	private String roomId;
	private String sender;
	private String uid;
	private Integer isSeat;
	private String empNo;
	private String message;
}
