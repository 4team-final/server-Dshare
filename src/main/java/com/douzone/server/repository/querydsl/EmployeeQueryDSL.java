//package com.douzone.server.repository.querydsl;
//
//import com.douzone.server.entity.Employee;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import static com.douzone.server.entity.QEmployee.employee;
//
//@Repository
//@RequiredArgsConstructor
//public class EmployeeQueryDSL {
//    private final JPAQueryFactory jpaQueryFactory;
//
//    public List<Employee> findEmployeeList(long positionId) {
//        return jpaQueryFactory.select(employee)
//                .from(employee).leftJoin(employee.position).fetchJoin()
//                .where(employee.position.id.eq(positionId)).fetch();
//    }
//}
