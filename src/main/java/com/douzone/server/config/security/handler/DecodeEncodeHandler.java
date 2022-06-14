package com.douzone.server.config.security.handler;

import com.douzone.server.entity.Employee;
import com.douzone.server.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DecodeEncodeHandler {
	private static final String METHOD_NAME = DecodeEncodeHandler.class.getName();
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final EmployeeRepository employeeRepository;

	@Autowired
	public DecodeEncodeHandler(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public String passwordEncode(String password) {
		return passwordEncoder.encode(password);
	}

	public String roleValid(String email) {
		log.info(METHOD_NAME + "- roleValid() ...");
		if (employeeRepository.existsByEmpNo(email)) {
			log.info("Employee EmpNo Validate - Success");
			Employee employee = employeeRepository.findByEmpNo(email);
			return employee.getRole();
		}
		log.warn("Employee EmpNo Validate - Fail");
		return null;
	}

	public boolean empNoValid(String empNo) {
		log.info(METHOD_NAME + "- emailValid() ...");
		try {
			Employee employee = employeeRepository.findByEmpNo(empNo);
			if (employee != null) {
				log.info("Employee Validate - Success");
				if (employee.getEmail() != null) {
					log.info("Employee EmpNo Validate - Success");
					return true;
				} else log.warn("Employee EmpNo Validate - Fail");
			} else log.warn("Employee Validate - Fail");
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return false;
	}

	public boolean passwordValid(String empNo, String password) {
		log.info(METHOD_NAME + "- passwordValid() ...");
		try {
			Employee employee = employeeRepository.findByEmpNo(empNo);
			if (passwordEncoder.matches(password, employee.getPassword())) {
				log.info("Password validate - Success");
				return true;
			} else log.warn("Password validate - Fail");
		} catch (Exception e) {
			log.error("SERVER ERROR " + METHOD_NAME, e);
		}
		return false;
	}
}
