package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBookmarkDTO {

    private int vehicleId;
    private int empId;

    public VehicleBookmark of() {
        return VehicleBookmark.builder()
                .vehicle(Vehicle.builder().id((long) vehicleId).build())
                .employee(Employee.builder().id((long) empId).build())
                .build();
    }
}