package com.douzone.server.service;

import com.douzone.server.dto.employee.SignModReqDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.exception.EmpNotFoundException;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class AdminServiceTest {
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired  private EmployeeRepository employeeRepository;


	@Test
	public void update() {
		String password = "12345678"; long id = 1;
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));
		try {
			if (passwordEncoder.matches(password, employee.getPassword())) {
				System.out.println("test success");
			} else {
				throw new Exception();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}