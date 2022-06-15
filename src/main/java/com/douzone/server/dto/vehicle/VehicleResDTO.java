package com.douzone.server.dto.vehicle;

import lombok.Builder;
import lombok.Getter;


/**
 * 0 : ok
 * 1 : bad request
 * 2 : server error
 */

@Getter
@Builder
public class VehicleResDTO {
	private int code;
	private Object data;
}
