package com.douzone.server.repository;

import com.douzone.server.entity.Department;
import com.douzone.server.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository  extends JpaRepository<Department, Long> {
}
