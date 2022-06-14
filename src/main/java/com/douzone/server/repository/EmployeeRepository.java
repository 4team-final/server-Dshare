package com.douzone.server.repository;

import com.douzone.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	Employee findByEmpNo(String empNo);

	boolean existsByEmpNo(String empNo);
}
