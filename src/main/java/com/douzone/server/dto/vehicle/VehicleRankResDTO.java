package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VehicleRankResDTO implements IVehicleRankResDTO {
	private Vehicle vehicle;
	private int vcount;
}
