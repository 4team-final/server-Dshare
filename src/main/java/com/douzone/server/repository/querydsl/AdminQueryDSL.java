package com.douzone.server.repository.querydsl;

import com.douzone.server.dto.employee.QSignModReqDTO;
import com.douzone.server.dto.employee.SignModReqDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.QPosition;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QTeam.team;
import static com.douzone.server.entity.QPosition.position;
import static com.douzone.server.entity.QDepartment.department;
@Repository
@RequiredArgsConstructor
public class AdminQueryDSL {
	private final JPAQueryFactory jpaQueryFactory;

}
