package com.douzone.server.repository;

import com.douzone.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmpNo(String empNo);

    boolean existsByEmpNo(String empNo);

    Optional<Employee> findTop1ByOrderByIdDesc();
}
