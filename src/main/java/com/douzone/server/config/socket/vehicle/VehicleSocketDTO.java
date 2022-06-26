package com.douzone.server.config.socket.vehicle;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude
public class VehicleSocketDTO {
	public enum MessageType {
		ENTER, TALK, DUAL, QUIT
	}

	private VehicleSocketDTO.MessageType type;
	private Long vehicleId;
	private String uid;
	private Integer[] time = new Integer[24];
	private String empNo;
	private String message;
}
