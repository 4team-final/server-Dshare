package com.douzone.server.service;

import com.douzone.server.dto.employee.EmpTeamDTO;
import com.douzone.server.dto.employee.EmpTestDTO;
import com.douzone.server.dto.employee.ProfileRes;
import com.douzone.server.dto.room.RoomBookmarkResDTO;
import com.douzone.server.dto.room.RoomResDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import com.douzone.server.repository.*;
import com.douzone.server.repository.querydsl.EmployeeQueryDSL;
import com.douzone.server.service.method.RoomServiceMethod;
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
	private final RoomRepository roomRepository;
	private final RoomImgRepository roomImgRepository;
	private final RoomObjectRepository roomObjectRepository;
	private final RoomServiceMethod roomServiceMethod;
	private final TeamRepository teamRepository;

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
	public ProfileRes readProfile(Long id) {

		List<Employee> MyInfoList = employeeQueryDSL.findMyProfile(id);
		ProfileRes MyInfo = MyInfoList.stream().map(employee -> {
			return ProfileRes.builder().build().of(employee, id);
		}).collect(Collectors.toList()).get(0);
		return MyInfo;
	}
	@Transactional
	public List<ProfileRes> readProfile() {
		List<Employee> MyInfoList = employeeQueryDSL.findProfile();
		List<ProfileRes> Info = MyInfoList.stream().map(employee -> {
			return ProfileRes.builder().build().of(employee);
		}).collect(Collectors.toList());
		return Info;
	}

	@Transactional
	public List<RoomResDTO> selectByMyBookmark(int empNo) {
		List<RoomBookmarkResDTO> roomBookmarkResDTOList = employeeQueryDSL.selectByMyBookmark(empNo);
		return roomServiceMethod.RoomImgListAndRoomObjectList(roomBookmarkResDTOList);
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

	@Transactional
	public List<EmpTeamDTO> readTeamInfo() {
		return teamRepository.findAll().stream().map(team -> {
			return EmpTeamDTO.builder().id(team.getId()).name(team.getName()).deptId(team.getDepartment().getId()).build();
		}).collect(Collectors.toList());
	}
}
