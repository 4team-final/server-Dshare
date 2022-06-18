package com.douzone.server.service;

import com.douzone.server.dto.employee.EmpTestDTO;
import com.douzone.server.dto.employee.ProfileDto;
import com.douzone.server.dto.room.RoomBookmarkDTO;
import com.douzone.server.entity.Employee;
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

	public List<RoomBookmarkDTO> selectByMyBookmark(int empNo) {
		return employeeQueryDSL.selectByMyBookmark(empNo);
	}
}
