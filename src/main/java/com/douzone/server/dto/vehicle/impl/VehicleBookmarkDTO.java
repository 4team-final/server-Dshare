package com.douzone.server.dto.vehicle.impl;

import com.douzone.server.dto.vehicle.jpainterface.IVehicleBookmarkDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBookmarkDTO implements IVehicleBookmarkDTO {
    private Long id;
    private Employee empId;
    private Vehicle vehicle;
}
