package com.douzone.server.repository;

import com.douzone.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmpNo(String empNo);

    boolean existsByEmpNo(String empNo);

    Optional<Employee> findTop1ByOrderByIdDesc();
}
