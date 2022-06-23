package com.douzone.server;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.dto.reservation.RegistReservationReqDto;
import com.douzone.server.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;



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
	@Autowired
	private MessageSource messageSource;

//	@Test
//	void 메세지() {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//
//		System.out.println(messageSource.getMessage("hello", null, null));
//		RegistReservationReqDto registReservationReqDto = new RegistReservationReqDto(
//				null, null, " ", " ", null, null);
//
//		Set<ConstraintViolation<RegistReservationReqDto>> violations = validator.validate(registReservationReqDto);
//
//		for (ConstraintViolation<RegistReservationReqDto> violation : violations) {
//			System.out.println("violation = " + violation);
//			System.out.println("violation.getMessage() = " + violation.getMessage());
//		}
//
//	}
//	@Test
//	void 사원등록() {
//		//when
//		signModReqDTO signModReqDTO = new signModReqDTO();
//		LocalDateTime localDateTime = LocalDateTime.now();
//		//?
//
////		String empNo = "1";
//		String empNo = "220100002";
//		signModReqDTO.builder()
//				.deptId(1)
//				.teamId(1)
//				.positionId(1)
//				.empNo(empNo)
//				.password(decodeEncodeHandler.passwordEncode("1234"))
//				.name("test")
//				.email("test@test.com")
//				.tel("00000000000")
//				.birthday(localDateTime)
//				.build();
//
//		//then
//		boolean exists = employeeRepository.existsByEmpNo(empNo);
//		if (exists) {
//			throw new EmpAlreadyExistException(ErrorCode.EMP_ALREADY_EXIST);
//		}
//		System.out.println("good");
//
//
//	}

}
