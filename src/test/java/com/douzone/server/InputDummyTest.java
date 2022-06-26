package com.douzone.server;


import com.douzone.server.config.socket.Calendar;
import com.douzone.server.config.socket.Time;
import com.douzone.server.config.socket.TimeRepository;
import com.douzone.server.entity.Employee;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.service.EmployeeService;
import com.douzone.server.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
public class InputDummyTest {
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	RoomService roomService;
	@Autowired
	TimeRepository timeRepository;

	/**
	 * 이름 바꾸기
	 */
	@Test
	void UpdateEmployeeName() {
		String[] LastName = {"김", "이", "박", "최", "정", "윤", "오", "신", "임", "황"};
		String[] firstName = {"동", "철", "규", "지", "원", "영", "수", "규", "래", "진", "성", "환", "우", "형", "윤", "재", "빈", "정", "민", "동", "훈"};
		String name = "";
		List<Employee> empList = employeeRepository.findAll();
		for (int i = 0; i < empList.size(); i++) {
			while (true) {
				String tmpLast = LastName[(int) (Math.random() * LastName.length)];
				String tmpFirst = firstName[(int) (Math.random() * firstName.length)];
				String tmpFirst2 = firstName[(int) (Math.random() * firstName.length)];
				if (!tmpFirst.equals(tmpFirst2) && !tmpLast.equals(tmpFirst) && !tmpLast.equals(tmpFirst2)) { //김동동, 김철철 방지
					name = tmpLast + tmpFirst + tmpFirst2;
					break;
				}
			}
			employeeRepository.updateName(name, empList.get(i).getId());
		}
	}

	/**
	 * 팀 바꾸기
	 */
	//48개팀 - 1번은 1팀 ~ 48번은 48팀, 49번은 1팀 ~ ...
	@Test
	void UpdateEmployeeTeam() {
		List<Employee> empList = employeeRepository.findAll();
		for (int i = 0; i < empList.size(); i++) {
			long t = (i + 1) % 48;
			if (t == 0) t = 48;
			employeeRepository.updateT(t, empList.get(i).getId());
		}
	}

	/**
	 * 전사원 랜덤으로 북마크 넣어주기 3번돌림
	 */
	@Test
	void InputRoomBookmark() {
		List<Employee> empList = employeeRepository.findAll();
		for (int i = 0; i < empList.size(); i++) {
			long roomId = (long) (Math.random() * 15) + 1L;
			employeeService.bookmarkRegisterAndDelete(roomId, empList.get(i).getId());
		}

	}

	@Test
	void InputRoomReservation() {
//		1~15번룸에
//
//		9시 부터 10시반 혹은 9시 부터 10시
//		11시 부터 12시 혹은 10시 30분 부터 12시반
//		13시 부터 15시 혹은 13시 부터 14시반
//		15시 부터 16시반 혹은 14시 30분 부터 16시
//		16시반 부터  18시 혹은 16시 부터 18시
//
//		승진 심사 회의 : 승진 심사를 위한 회의실 대여
//		erp10 백앤드 개발 회의 :  백앤드 페어프로그래밍을 위한 회의실 대여
//		신사업 예산 검토 회의 :  신사업의 사업적합도를 평가하기 위한 임원급 회의 입니다.
//		d-share 개발부서 신입사원 채용회의 :  신입사원 채용 방식에 대한 토론입니다.
//		외국 지부 회의 :  두바이, 미국, 영국 등 더존의 해외 지부와 결탁한 거래처 직원과의 회의입니다.
//		개발 부서 코드리뷰 : 개발부 임직원 실력향상과 소양함양을 위한 코드리뷰 회의입니다.
//		zoom meeting : 코로나로 인한 비대면 회의를 위해 회의실을 잡았습니다.
//		상여금제도 기획 회의 : 상여금 제도 구성을 위해 인사부 차장 이상급이 진행하는 회의입니다.
//		마케팅부 정기회의 : 1~4분기 분기별 마케팅 성과 검토 및 향후 기획 회의
//		영업실적 검토회의 : 영업 평가 및 영업활동 개선에 대한 회의
		for (int i = 0; i < 15; i++) {

		}


	}

	@Test
	void InputVehicleReservation() {

	}

	//타임테이블에 시간넣기
	@Test
	void InputTimeTable() {
		String[] timeTable =
				{"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30"
						, "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"};

		//1번~15번 방
		for (int i = 1; i <= +15; i++) {
			String uid = "20220623";
			for (int j = 1; j <= 21; j++) {
				if ((uid.charAt(6) + "" + uid.charAt(7)).equals("30")) {
					uid = "20220700";
				}
				uid = Integer.parseInt(uid) + 1 + "";
				System.out.println(uid);
				for (int k = 0; k < 18; k++) {
					timeRepository.save(Time.builder()
									.calendar(Calendar.builder().uid(uid).build())
									.isSeat(0).time(timeTable[k]).roomId(i).build());
				}
			}
		}
	}
}
