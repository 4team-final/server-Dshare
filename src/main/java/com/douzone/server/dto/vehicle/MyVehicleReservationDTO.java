package com.douzone.server.dto.vehicle;

import com.douzone.server.dto.vehicle.impl.VehicleEmpResDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyVehicleReservationDTO {
	private List<VehicleEmpResDTO> beforeList;
	private List<VehicleEmpResDTO> afterList;


	@Builder
	public MyVehicleReservationDTO(List<VehicleEmpResDTO> beforeList, List<VehicleEmpResDTO> afterList) {
		this.beforeList = beforeList;
		this.afterList = afterList;
	}

	public MyVehicleReservationDTO of(List<VehicleEmpResDTO> beforeList, List<VehicleEmpResDTO> afterList) {
		return MyVehicleReservationDTO.builder()
				.beforeList(beforeList)
				.afterList(afterList)
				.build();
	}
}
