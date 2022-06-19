package com.douzone.server.service;

import com.douzone.server.dto.employee.EmpTestDTO;
import com.douzone.server.dto.employee.ProfileDto;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
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

	@Transactional
	public List<EmpTestDTO> queryDSLTest(long positionId) {
		List<Employee> employeeList = employeeQueryDSL.findEmployeeList(positionId);
		List<EmpTestDTO> empTestDTOList = employeeList.stream().map(employee -> {
			EmpTestDTO empTestDTO = EmpTestDTO.builder().build().of(employee);
			return empTestDTO;
		}).collect(Collectors.toList());
		return empTestDTOList;
	}

	@Transactional
	public ProfileDto readProfile(long id) {
		List<Employee> MyInfoList = employeeQueryDSL.findMyProfile(id);
		ProfileDto MyInfo = MyInfoList.stream().map(employee -> {
			return ProfileDto.builder().build().of(employee);
		}).collect(Collectors.toList()).get(0);
		return MyInfo;
	}

	@Transactional
	public List<RoomBookmarkResDTO> selectByMyBookmark(int empNo) {
		return employeeQueryDSL.selectByMyBookmark(empNo);
	}

	@Transactional
	public Long bookmarkRegisterAndDelete(long roomId, long empId) {
		if (roomBookmarkRepository.existsByMeetingRoom_IdAndEmployee_Id(roomId, empId)) {
			roomBookmarkRepository.deleteByMeetingRoom_IdAndEmployee_Id(roomId, empId);
			return 2L;
		} else {
			roomBookmarkRepository.save(RoomBookmark.builder().meetingRoom(MeetingRoom.builder().id(roomId).build()).employee(Employee.builder().id(empId).build()).build()).getId();
			return 1L;
		}
	}
}
