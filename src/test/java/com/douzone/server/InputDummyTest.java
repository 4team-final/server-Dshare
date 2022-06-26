package com.douzone.server;

import com.douzone.server.dto.room.RoomObjectReqDTO;
import com.douzone.server.dto.room.RoomReqDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.Team;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.service.EmployeeService;
import com.douzone.server.service.RoomService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
public class InputDummyTest {
	@Autowired EmployeeRepository employeeRepository;
	@Autowired EmployeeService employeeService;
	@Autowired RoomService roomService;

	/**
	 * 이름 바꾸기
	 */
	@Test
	void UpdateEmployeeName() {
		String[] LastName = {"김", "이","박","최","정", "윤" ,"오", "신", "임", "황"};
		String[] firstName = {"동", "철", "규", "지","원", "영" ,"수", "규", "래", "진", "성","환","우", "형", "윤", "재", "빈", "정", "민", "동", "훈"};
		String name = "";
		List<Employee> empList =  employeeRepository.findAll();
		for(int i = 0 ; i < empList.size() ; i ++){
			while(true) {
				String tmpLast = LastName[(int) (Math.random() * LastName.length)];
				String tmpFirst = firstName[(int) (Math.random() * firstName.length)];
				String tmpFirst2 = firstName[(int) (Math.random() * firstName.length)];
				if(!tmpFirst.equals(tmpFirst2) && !tmpLast.equals(tmpFirst) && !tmpLast.equals(tmpFirst2)) { //김동동, 김철철 방지
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
		List<Employee> empList =  employeeRepository.findAll();
		for(int i = 0 ; i < empList.size() ; i ++){
			long t = (i+1)%48;
			if(t==0) t=48;
			employeeRepository.updateT(t, empList.get(i).getId());
		}
	}
	/**
	 * 전사원 랜던으로 북마크 넣어주기 3번돌림
	 */
	@Test
	void InputRoomBookmark() {
		List<Employee> empList =  employeeRepository.findAll();
		for(int i = 0 ; i < empList.size() ; i ++){
			long roomId = (long)(Math.random() * 15)+1L;
			employeeService.bookmarkRegisterAndDelete(roomId, empList.get(i).getId());
		}

	}


	@Test
	void InputRoomReservation () {

	}
	@Test
	void InputVehicleReservation() {

	}

}
