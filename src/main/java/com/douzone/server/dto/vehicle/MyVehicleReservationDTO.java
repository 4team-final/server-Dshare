package com.douzone.server.dto.vehicle;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleEmpResDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyVehicleReservationDTO {
	private List<IVehicleEmpResDTO> beforeList;
	private List<IVehicleEmpResDTO> afterList;

	@Builder
	public MyVehicleReservationDTO(List<IVehicleEmpResDTO> beforeList, List<IVehicleEmpResDTO> afterList) {
		this.beforeList = beforeList;
		this.afterList = afterList;
	}

	public MyVehicleReservationDTO of(List<IVehicleEmpResDTO> beforeList, List<IVehicleEmpResDTO> afterList) {
		return MyVehicleReservationDTO.builder()
				.beforeList(beforeList)
				.afterList(afterList)
				.build();
	}
}
