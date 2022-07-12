package com.douzone.server;


import com.douzone.server.config.socket.TimeRepository;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.RoomReservationRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import com.douzone.server.service.EmployeeService;
import com.douzone.server.service.RoomService;
import com.douzone.server.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@SpringBootTest
@Transactional
public class InputDummyTest {
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	EmployeeService employeeService;
	@Autowired
	RoomService roomService;
	@Autowired
	TimeRepository timeRepository;
	@Autowired
	VehicleRepository vehicleRepository;
	@Autowired
	VehicleService vehicleService;

	@Autowired
	RoomReservationRepository roomReservationRepository;

	@Autowired
	VehicleReservationRepository vehicleReservationRepository;
//
//	/**
//	 * 이름 바꾸기
//	 */
//	@Test
//	void UpdateEmployeeName() {
//		String[] LastName = {"김", "이", "박", "최", "정", "윤", "오", "신", "임", "황"};
//		String[] firstName = {"동", "철", "규", "지", "원", "영", "수", "규", "래", "진", "성", "환", "우", "형", "윤", "재", "빈", "정", "민", "동", "훈"};
//		String name = "";
//		List<Employee> empList = employeeRepository.findAll();
//		for (int i = 0; i < empList.size(); i++) {
//			while (true) {
//				String tmpLast = LastName[(int) (Math.random() * LastName.length)];
//				String tmpFirst = firstName[(int) (Math.random() * firstName.length)];
//				String tmpFirst2 = firstName[(int) (Math.random() * firstName.length)];
//				if (!tmpFirst.equals(tmpFirst2) && !tmpLast.equals(tmpFirst) && !tmpLast.equals(tmpFirst2)) { //김동동, 김철철 방지
//					name = tmpLast + tmpFirst + tmpFirst2;
//					break;
//				}
//			}
//			employeeRepository.updateName(name, empList.get(i).getId());
//		}
//	}
//
//	/**
//	 * 팀 바꾸기
//	 */
//	//48개팀 - 1번은 1팀 ~ 48번은 48팀, 49번은 1팀 ~ ...
//	@Test
//	void UpdateEmployeeTeam() {
//		List<Employee> empList = employeeRepository.findAll();
//		for (int i = 0; i < empList.size(); i++) {
//			long t = (i + 1) % 48;
//			if (t == 0) t = 48;
//			employeeRepository.updateT(t, empList.get(i).getId());
//		}
//	}
//
//	/**
//	 * 전사원 랜덤으로 회의실 북마크 넣어주기 3번돌림
//	 */
//	@Test
//	void InputRoomBookmark() {
//		List<Employee> empList = employeeRepository.findAll();
//		for (int i = 0; i < empList.size(); i++) {
//			long roomId = (long) (Math.random() * 15) + 1L;
//			employeeService.bookmarkRegisterAndDelete(roomId, empList.get(i).getId());
//		}
//	}
//	/**
//	 * 전사원 랜덤으로 차량 북마크 넣어주기 1번만됨
//	 */
//	@Test
//	void InputVehicleBookmark() {
//		List<Employee> empList = employeeRepository.findAll();
//		for (int i = 0; i < empList.size(); i++) {
//			long vehicleId = (long) (Math.random() * 10) + 1L;
//			vehicleService.registerByVehicleBookmark(empList.get(i).getId(),vehicleId);
//		}
//	}
//
//	/**
//	 * 회의실 예약 넣기
//	 */
//	@Test
//	void InputRoomReservation() {
//		String[][] times = {
//				{"09:00~10:30", "09:00~10:00"},
//				{"11:00~12:00", "10:30~12:30"},
//				{"13:00~14:00", "13:00~14:30"},
//				{"15:00~16:00", "15:00~15:30"},
//				{"16:30~17:30", "16:30~17:30"}
//		};
//		String[] meetings = {
//				"승진 심사 회의 : 승진 심사를 위한 회의실 대여",
//				"erp10 백앤드 개발 회의 : 백앤드 페어프로그래밍을 위한 회의실 대여",
//				"신사업 예산 검토 회의 : 신사업의 사업적합도를 평가하기 위한 임원급 회의 입니다.",
//				"d-share 개발부서 신입사원 채용회의 : 신입사원 채용 방식에 대한 토론입니다.",
//				"외국 지부 회의 : 두바이, 미국, 영국 등 더존의 해외 지부와 결탁한 거래처 직원과의 회의입니다.",
//				"개발 부서 코드리뷰 : 개발부 임직원 실력향상과 소양함양을 위한 코드리뷰 회의입니다.",
//				"zoom meeting : 코로나로 인한 비대면 회의를 위해 회의실을 잡았습니다.",
//				"상여금제도 기획 회의 : 상여금 제도 구성을 위해 인사부 차장 이상급이 진행하는 회의입니다.",
//				"마케팅부 정기회의 : 1~4분기 분기별 마케팅 성과 검토 및 향후 기획 회의",
//				"영업실적 검토회의 : 영업 평가 및 영업활동 개선에 대한 회의"
//		};
//
//		for (int i = 1; i <= 15; i++) { //1에서 15번방
//			String uid = "20220623";
//
//				for(int k = 1 ; k <=22 ; k ++) {
//					if ((uid.charAt(6) + "" + uid.charAt(7)).equals("30")) {
//						uid = "20220700";
//					}
//					uid = Integer.parseInt(uid) + 1 + "";
//
//					for(int j = 0; j < times.length ; j++){ //5개 타임을 순서대로 선택
//
//						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
//						String time = times[j][(int) (Math.random() * 2)];//0~1
//						String[] startEndTime  = time.split("~");
//						LocalDateTime startTime = LocalDateTime.parse(uid+" "+startEndTime[0]+":00", formatter );
//						LocalDateTime endTime = LocalDateTime.parse(uid+" "+startEndTime[1]+":00", formatter);
//
//						String[] meeting = meetings[(int)(Math.random()*meetings.length)].split(" : ");
//						RegistReservationReqDto registReservationReqDto = RegistReservationReqDto.builder()
//								.reason(meeting[1]).title(meeting[0]).roomId(Long.valueOf(i)).empId(Long.valueOf((int)(Math.random()*300)+50)).startedAt(startTime).endedAt(endTime).build();
//
//						roomService.save(registReservationReqDto);
//				}
//
//			}
//		}
//
//
//	}
//
//	/**
//	 * 차량 예약 넣기
//	 */
//	@Test
//	void InputVehicleReservation() {
//		//10대의차
//		//22일동안
//		//300명의 사원
//		//1박2일 2박 3일 당일치기로 구분
//		String[][] times = {
//				{"09:00:00", "11:00:00", "13:00:00", "15:00:00", "16:30:00", "17:00:00", "18:00:00", "19:00:00", "20:30:00", "21:00:00", "22:00:00"},
//				{"1","2","3","4","5"}
//		};
//		String[] vehicles = {
//				"전북 출장 : 더존비즈온 전북 지사 출장",
//				"경기 출장 : 더존비즈온 경기 지사 출장",
//				"부산 출장 : 더존비즈온 부산 지사 출장",
//				"광주 출장 : 더존비즈온 광주 지사 출장",
//				"강원 출장 : 더존비즈온 강원 지사 출장",
//				"충청 출장 : 더존비즈온 충청 지사 출장",
//				"제주 출장 : 더존비즈온 제주 지사 출장",
//				"서울 출장 : 더존비즈온 서울 지사 출장",
//
//		};
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		List<Employee> empList = employeeRepository.findAll();
//
//
//		for(int i = 1 ; i <= 10 ; i ++) {
//			String uid = "20220623";
//			int day = 22;
//			for(int k = 1 ; k <=day ; k ++) { //7/15
//				String startDay = "";
//				String endDay = "";
//				if ((uid.charAt(6) + "" + uid.charAt(7)).equals("30")) {
//					uid = "20220700";
//				}
//				uid = Integer.parseInt(uid) + 1 + "";//0624
//				for(int j = 0; j < times.length ; j++){
//					//시작시간 끝시간 며칠빌릴지 정해줍니다.
//					String start = times[0][(int) (Math.random() * times[0].length)];
//					String end = times[0][(int) (Math.random() * times[0].length)];
//					int days = Integer.parseInt(times[1][(int) (Math.random() * times[1].length)]);
//					System.out.println(days);
//					if(Integer.parseInt((uid.charAt(6) + "" + uid.charAt(7)))+days <= 30) {
//						startDay =( Integer.parseInt(uid)+1)+"";
//
//						endDay = (Integer.parseInt(uid) + days)+"";
//						if(endDay.equals(startDay)){
//							endDay = (Integer.parseInt(endDay)+1)+"";
//						}
//						uid = endDay;
//						day -= days;
//
//						String yyyy = startDay.substring(0,4);
//						String mm = startDay.substring(4,6);
//						String dd = startDay.substring(6);
//
//
//						String yyyy2 = endDay.substring(0,4);
//						String mm2 = endDay.substring(4,6);
//						String dd2 = endDay.substring(6);
//
//						String formatUid = yyyy+"-"+ mm + "-" +dd;
//						String formatUid2 = yyyy2+"-"+ mm2 + "-" +dd2;
//						System.out.println(formatUid2);
//						String startTime = formatUid+" "+ start;
//						String endTime = formatUid2+" " + end;
//
//
//
//						String[] vehicle = vehicles[1].split(" : ");
//						long empId = empList.get((int)(Math.random()*300)+15).getId();
//
//						VehicleParseDTO vehicleParseDTO = VehicleParseDTO.builder()
//								.vehicleId(Long.valueOf(i))
//								.empId((int)empId)
//								.startedAt(startTime)
//								.endedAt(endTime)
//								.title(vehicle[0])
//								.reason(vehicle[1])
//								.build();
//
//						vehicleService.registerByVehicleReservation(vehicleParseDTO, empId);
//					}
//
//				}
//			}
//		}
//	}
//
//	//타임테이블에 시간넣기
//	@Test
//	void InputTimeTable() {
//		String[] timeTable =
//				{"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30"
//						, "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30"};
//		//1번~15번 방
//		for (int i = 1; i <= +15; i++) {
//			String uid = "20220623";
//			for (int j = 1; j <= 22; j++) {
//				if ((uid.charAt(6) + "" + uid.charAt(7)).equals("30")) {
//					uid = "20220700";
//				}
//				uid = Integer.parseInt(uid) + 1 + "";
//				System.out.println(uid);
//				for (int k = 0; k < 18; k++) {
//					timeRepository.save(Time.builder()
//									.calendar(Calendar.builder().uid(uid).build())
//									.isSeat(0).time(timeTable[k]).roomId(i).build());
//				}
//			}
//		}
//	}

	// modifiedAt 넣기
//	@Test
////	@Rollback(false)
//	void InputModifiedAtRoom() {
//
//		for (int i = 1; i <= 622; i++) {
//			String startedAt = roomReservationRepository.TestStartedAt((long) i);
//			if(startedAt == null || startedAt.equals("")|| startedAt==null)continue;
//			Random random = new Random();
//			long minusTemp = random.nextInt(5);
//			long minusTemp2 = random.nextInt(7);
//			long minusTemp3 = random.nextInt(59);
//			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//
//			LocalDateTime aa = LocalDateTime.parse(startedAt, format).minusDays(minusTemp).minusHours(minusTemp2).minusMinutes((minusTemp3));
//
//			roomReservationRepository.TestUpdateModified(aa, aa, (long) i);
//		}
////		String startedAt = vehicleReservationRepository.TestStartedAt((2L));
////		String startedAt2 = vehicleReservationRepository.TestStartedAt((1L));
////		System.out.println("asdas");
////		System.out.println(startedAt);
////		System.out.println(startedAt2);
//	System.out.println("asdas");
//		for (int i = 1; i <= 1000; i++) {
//
//			String startedAt = vehicleReservationRepository.TestStartedAt((long) i);
//			System.out.println();
//			if(startedAt == null || startedAt.equals("") || startedAt.equals(null))continue;
//			Random random = new Random();
//			long minusTemp = random.nextInt(5);
//			long minusTemp2 = random.nextInt(7);
//			long minusTemp3 = random.nextInt(59);
//
//			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//			LocalDateTime aa = LocalDateTime.parse(startedAt, format).minusDays(minusTemp).minusHours(minusTemp2).minusMinutes((minusTemp3));
//
//			vehicleReservationRepository.TestUpdateModified(aa, aa, (long) i);
//		}
//	}
}
