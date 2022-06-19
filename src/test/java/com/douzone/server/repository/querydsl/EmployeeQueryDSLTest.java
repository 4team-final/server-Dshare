package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EmployeeQueryDSLTest {

	@Autowired
	private EmployeeQueryDSL employeeQueryDSL;

	/**
	 * 임의로 사원/팀/부서까지 다만들기 힘들어서 given에 임의로 조회 값을 넣어서 테스트 했습니다.
	 */
	@Test
	void 직원조회() {
		//given
		String empNo = "220100001";
		String team = "1cell";
		String dept = "관리부";
		String position = "회장";
		//when
		List<Employee> list = employeeQueryDSL.findEmployeeList(1);
		//then
		assertThat(list.get(0).getEmpNo()).isEqualTo(empNo);
		assertThat(list.get(0).getTeam().getName()).isEqualTo(team);
		assertThat(list.get(0).getTeam().getDepartment().getName()).isEqualTo(dept);
		assertThat(list.get(0).getPosition().getName()).isEqualTo(position);
//		System.out.println(employeeQueryDSL.findEmployeeList(1).get(0));
	}


}