package com.douzone.server.service;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.exception.EmpAlreadyExistException;
import com.douzone.server.exception.EmpNotFoundException;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private static final String METHOD_NAME = VehicleService.class.getName();
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
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

    public ResponseDTO createReservation(VehicleReservationDTO vehicleReservationDTO, Long empId,Long vId) {
        log.info(METHOD_NAME + "-createReservation");
        try {

            VehicleReservation vehicleReservation =
                    VehicleReservation.builder()
                            .vehicle(Vehicle.builder().id(vId).build())
                            .employee(Employee.builder().id(empId).build())
                            .reason(vehicleReservationDTO.getReason())
                            .title(vehicleReservationDTO.getTitle())
                            .startedAt(vehicleReservationDTO.getStartedAt())
                            .endedAt(vehicleReservationDTO.getEndedAt())
                            .build();

            vehicleReservationRepository.save(vehicleReservation);

            return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_RESERVE);
        } catch (Exception e) {
            log.error("SERVER ERROR", e);
        }
        return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_RESERVE);
    }
}
