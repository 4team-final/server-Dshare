package com.douzone.server.service;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.exception.EmpAlreadyExistException;
import com.douzone.server.exception.EmpNotFoundException;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final EmployeeRepository employeeRepository;
    private final VehicleReservationRepository vehicleReservationRepository;
    private final DecodeEncodeHandler decodeEncodeHandler;
    @Value(value = "${year.current}")
    private String year;

    @Transactional
    public Long register(SignupReqDTO signupReqDTO) {

        Employee employee = employeeRepository.findTop1ByOrderByIdDesc().orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

        //년도 + 부서 + 사번
        StringBuilder sb = new StringBuilder();
        String deptId = String.format("%02d", signupReqDTO.getDeptId());
        String empId = String.format("%05d", employee.getId() + 1);
        sb.append(year).append(deptId).append(empId);

        String empNo = sb.toString();
        boolean exists = employeeRepository.existsByEmpNo(empNo);
        if (exists) {
            throw new EmpAlreadyExistException(ErrorCode.EMP_ALREADY_EXIST);
        }
        String password = decodeEncodeHandler.passwordEncode(signupReqDTO.getPassword());
        long id = employeeRepository.save(signupReqDTO.of(empNo, password)).getId();

        return id;
    }

    public Integer createVehicleReservation(VehicleReservationDTO vehicleReservationDTO) {
        try {
            VehicleReservation vehicleReservation = VehicleReservation.builder()
                    .vehicle(Vehicle.builder().id(1L).build())
                    .employee(Employee.builder().id(1L).build())
                    .reason(vehicleReservationDTO.getReason())
                    .title(vehicleReservationDTO.getTitle())
                    .build();
            VehicleReservation result = vehicleReservationRepository.save(vehicleReservation);
            if(result.getId() == null) return 0;
            return 1;
        } catch (Exception e) {
            log.error("SERVER ERROR", e);
        }
        return null;
    }
}
