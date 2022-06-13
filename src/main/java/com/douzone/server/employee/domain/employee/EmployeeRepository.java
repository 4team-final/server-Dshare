package com.douzone.server.employee.domain.employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Employee findByEmpNo(String empNo);
	boolean existsByEmpNo(String empNo);
}
