package com.douzone.server.service;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.config.utils.UploadDTO;
import com.douzone.server.config.utils.UploadUtils;
import com.douzone.server.dto.employee.SignupReqDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.exception.EmpAlreadyExistException;
import com.douzone.server.exception.EmpNotFoundException;
import com.douzone.server.exception.ErrorCode;
import com.douzone.server.exception.ImgFileNotFoundException;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
	private static final String METHOD_NAME = VehicleService.class.getName();
	private final EmployeeRepository employeeRepository;
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final DecodeEncodeHandler decodeEncodeHandler;
	private final UploadUtils uploadUtils;

	@Value(value = "${year.current}")
	private String year;

	@Transactional
	public Long register(SignupReqDTO signupReqDTO) {

		Employee employee = employeeRepository.findTop1ByOrderByIdDesc().orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

		//년도 + 부서 + 사번
		StringBuilder sb = new StringBuilder();
		String deptId = String.format("%02d", signupReqDTO.getDeptId());
		String empId = String.format("%05d", employee.getId() + 1);
		sb.append(year).append(deptId).append(empId);

		String empNo = sb.toString();
		boolean exists = employeeRepository.existsByEmpNo(empNo);
		if (exists) {
			throw new EmpAlreadyExistException(ErrorCode.EMP_ALREADY_EXIST);
		}
		String password = decodeEncodeHandler.passwordEncode(signupReqDTO.getPassword());
		long id = employeeRepository.save(signupReqDTO.of(empNo, password)).getId();

		return id;
	}

	@Transactional
	public Long uploadProfileImg(List<MultipartFile> files, long id) {
		String basePath = "profile/";
		List<UploadDTO> uploadDTOS = uploadUtils.upload(files, basePath);
		if (uploadDTOS == null) {
			throw new ImgFileNotFoundException(ErrorCode.IMG_NOT_FOUND);
		}
		String profileImg = uploadDTOS.get(0).getPath();
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

		employee.updateProfileImg(profileImg);
		return employee.getId();
	}
}
