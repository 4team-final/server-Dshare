package com.douzone.server.admin.service;

import com.douzone.server.admin.dto.employee.SignupReqDTO;
import com.douzone.server.admin.exception.EmpAlreadyExistException;
import com.douzone.server.admin.exception.EmpNotFoundException;
import com.douzone.server.admin.exception.ErrorCode;
import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.employee.domain.employee.Employee;
import com.douzone.server.employee.domain.employee.EmployeeRepository;
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
}
