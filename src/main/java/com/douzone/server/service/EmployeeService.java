package com.douzone.server.service;

import com.douzone.server.dto.employee.EmpTestDTO;
import com.douzone.server.dto.employee.ProfileDto;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.RoomBookmarkRepository;
import com.douzone.server.repository.querydsl.EmployeeQueryDSL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

	private final EmployeeQueryDSL employeeQueryDSL;
	private final EmployeeRepository employeeRepository;
	private final RoomBookmarkRepository roomBookmarkRepository;


	public List<EmpTestDTO> queryDSLTest(long positionId) {
		List<Employee> employeeList = employeeQueryDSL.findEmployeeList(positionId);
		List<EmpTestDTO> empTestDTOList = employeeList.stream().map(employee -> {
			EmpTestDTO empTestDTO = EmpTestDTO.builder()
					.build().of(employee);
			return empTestDTO;
		}).collect(Collectors.toList());
		return empTestDTOList;
	}

	public ProfileDto readProfile(long id) {
		List<Employee> MyInfoList = employeeQueryDSL.findMyProfile(id);
		ProfileDto MyInfo = MyInfoList.stream().map(employee -> {
			return ProfileDto.builder().build().of(employee);
		}).collect(Collectors.toList()).get(0);
		return MyInfo;
	}

	public List<RoomBookmarkResDTO> selectByMyBookmark(int empNo) {
		return employeeQueryDSL.selectByMyBookmark(empNo);
	}

//	public Long bookmarkRegister(RoomBookmarkReqDTO roomBookmarkReqDTO) {
//		if (roomBookmarkRepository.existsByMeetingRoom_IdAndEmployee_Id()) {
//
//		}
//		return roomBookmarkRepository.save(roomBookmarkReqDTO.of()).getId();
//	}
}
