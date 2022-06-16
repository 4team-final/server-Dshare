package com.douzone.server.repository.querydsl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional()
class EmployeeQueryDSLTest {

	@Autowired
	private EmployeeQueryDSL employeeQueryDSL;

	@Test
	void 가장최근예약3개조회() {
		//when, then


		for(int i = 0 ; i < 3 ; i++) {
			System.out.println(employeeQueryDSL.reservedLatestRoomTop3(3).get(i).getTitle()+i+"번");
		}
	}


}