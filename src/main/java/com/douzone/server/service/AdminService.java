package com.douzone.server.service;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.config.utils.UploadDTO;
import com.douzone.server.config.utils.UploadUtils;
import com.douzone.server.dto.employee.SignModReqDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Team;
import com.douzone.server.exception.*;
import com.douzone.server.repository.*;
import com.douzone.server.repository.querydsl.AdminQueryDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
	private static final String METHOD_NAME = VehicleService.class.getSimpleName();
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final EmployeeRepository employeeRepository;
	private final TeamRepository teamRepository;
	private final PositionRepository positionRepository;
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final DecodeEncodeHandler decodeEncodeHandler;
	private final UploadUtils uploadUtils;
	private final AdminQueryDSL adminQueryDSL;

	@Value(value = "${year.current}")
	private String year;

	@Transactional
	public Long register(SignModReqDTO signModReqDTO) {

		Employee employee = employeeRepository.findTop1ByOrderByIdDesc().orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

		//년도 + 부서 + 사번
		String empNo = signModReqDTO.makeEmpno(employee, year).toString();

		boolean exists = employeeRepository.existsByEmpNo(empNo);

		if (exists) {
			throw new EmpAlreadyExistException(ErrorCode.EMP_ALREADY_EXIST);
		}
		String password = decodeEncodeHandler.passwordEncode(signModReqDTO.getPassword());
		long id = employeeRepository.save(signModReqDTO.of(empNo, password)).getId();

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

	/**
	 * 관리자에 의한 프로필 수정, 관리자에 의한 비밀번호 변경은 포함 x
	 */
	@Transactional
	public long update(SignModReqDTO signModReqDTO, long id) throws RuntimeException {

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

		long teamId = signModReqDTO.getTeamId(); long positionId = signModReqDTO.getPositionId();
		if(teamId > teamRepository.findLastTeam() && teamId <= 0) new DataIntegrityViolationException(teamId+"가 없습니다.");
		if(positionId > positionRepository.findLastPosition() && teamId <= 0) new DataIntegrityViolationException(positionId+"가 없습니다.");

		employee.update(signModReqDTO);
		employeeRepository.updateTP(teamId, positionId, employee.getId());

		return employee.getId();
	}
	/**
	 * 관리자에 의한 비밀번호 변경
	 * */
	@Transactional
	public long updatePw(SignModReqDTO signModReqDTO, long id) throws RuntimeException {
		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

		//관리자에 의한 비밀번호 변경
		if (passwordEncoder.matches(signModReqDTO.getOriginPassword(), employee.getPassword())) {
				log.info("기존 패스워드가 일치 합니다 기존 패스워드 : {} ", employee.getPassword());
		} else {
			throw new PasswordNotMatchException(ErrorCode.PW_NOT_MATCH);
		}

		String newPassword = decodeEncodeHandler.passwordEncode(signModReqDTO.getNewPassword());
		employee.update(newPassword);

		return employee.getId();
	}


}
