package com.douzone.server;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.exception.EmpAlreadyExistException;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.repository.EmployeeRepository;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * admin test
 *
 * 사원등록 test
 */
@SpringBootTest
@Transactional
class ServerApplicationTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private DecodeEncodeHandler decodeEncodeHandler;

	@Test
	void 사원등록() {
		//when
		SignupReqDTO signupReqDTO = new SignupReqDTO();
		LocalDateTime localDateTime = LocalDateTime.now();
		//?

//		String empNo = "1";
		String empNo = "220100002";
		signupReqDTO.builder()
				.deptId(1)
				.teamId(1)
				.positionId(1)
				.empNo(empNo)
				.password(decodeEncodeHandler.passwordEncode("1234"))
				.name("test")
				.email("test@test.com")
				.tel("00000000000")
				.birthday(localDateTime)
				.build();

		//then
		boolean exists = employeeRepository.existsByEmpNo(empNo);
		if (exists) {
			throw new EmpAlreadyExistException(ErrorCode.EMP_ALREADY_EXIST);
		}
		System.out.println("good");


	}

}
