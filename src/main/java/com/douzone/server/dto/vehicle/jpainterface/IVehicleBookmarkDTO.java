package com.douzone.server.dto.vehicle.jpainterface;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;

public interface IVehicleBookmarkDTO {
    Long getId();

    Employee getEmpId();

    Vehicle getVehicle();
}
