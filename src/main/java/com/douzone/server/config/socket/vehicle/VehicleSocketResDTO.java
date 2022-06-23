package com.douzone.server.config.socket.vehicle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehicleSocketResDTO {
	private String roomId;
	private String name;
	private Integer size;
}
