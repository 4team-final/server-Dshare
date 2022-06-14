package com.douzone.server.dto.vehicle;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReservationDTO {
    private int vehicleId;
    private int empId;
    private String reason;
    private String title;

    public VehicleReservation of() {
        return VehicleReservation.builder()
            .vehicle(Vehicle.builder().id((long)vehicleId).build())
            .employee(Employee.builder().id((long)empId).build())
                .reason(reason)
                .title(title)
            .build();
    }
}
