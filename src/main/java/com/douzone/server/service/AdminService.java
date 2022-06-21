package com.douzone.server.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.config.utils.UploadDTO;
import com.douzone.server.config.utils.UploadUtils;
import com.douzone.server.dto.employee.SignModReqDTO;
import com.douzone.server.dto.reservation.ReservationResDTO;
import com.douzone.server.dto.room.RoomImgResDTO;
import com.douzone.server.dto.room.RoomObjectResDTO;
import com.douzone.server.dto.room.RoomReservationSearchDTO;
import com.douzone.server.dto.vehicle.VehicleImgDTO;
import com.douzone.server.dto.vehicle.VehicleUpdateDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleImg;
import com.douzone.server.exception.*;
import com.douzone.server.repository.*;
import com.douzone.server.repository.querydsl.AdminQueryDSL;
import com.douzone.server.repository.querydsl.RoomQueryDSL;
import com.douzone.server.service.method.ServiceMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.douzone.server.config.utils.Msg.*;


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
	private final VehicleImgRepository vehicleImgRepository;
	private final DecodeEncodeHandler decodeEncodeHandler;
	private final UploadUtils uploadUtils;
	private final AdminQueryDSL adminQueryDSL;
	private final RoomQueryDSL roomQueryDSL;
	private final RoomService roomService;
	private final ServiceMethod serviceMethod;

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

	@Transactional
	public ResponseDTO createVehicle(VehicleUpdateDTO vehicleUpdateDTO, List<MultipartFile> files) {
		log.info(METHOD_NAME + "- createVehicle");

		return Optional.of(new ResponseDTO())
				.filter(u -> !files.isEmpty())
				.map(res -> {
					VehicleImgDTO vehicleImgDTO = updateVehicleImg(files);

					Long vId = vehicleRepository.save(Vehicle.builder()
							.name(vehicleUpdateDTO.getName())
							.number(vehicleUpdateDTO.getNumber())
							.model(vehicleUpdateDTO.getModel())
							.color(vehicleUpdateDTO.getColor())
							.capacity(vehicleUpdateDTO.getCapacity())
							.build()).getId();

					vehicleImgRepository.save(VehicleImg.builder()
							.vehicle(Vehicle.builder().id(vId).build())
							.path(vehicleImgDTO.getPath())
							.type(vehicleImgDTO.getType())
							.imgSize(vehicleImgDTO.getImgSize())
							.build());
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RESISTER);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RESISTER));
	}

	@Transactional
	public ResponseDTO updateVehicle(VehicleUpdateDTO vehicleUpdateDTO, Long id, List<MultipartFile> files) {
		log.info(METHOD_NAME + "- updateVehicle");

		return Optional.of(new ResponseDTO())
				.filter(u -> id > 0L)
				.map(v -> vehicleRepository.findById(id))
				.filter(Optional::isPresent)
				.map(res -> res.get().updateVehicle(vehicleUpdateDTO))
				.map(vehicleImgRepository::findByVehicleId)
				.filter(Optional::isPresent)
				.map(data -> {
					String[] path = data.get().getPath().split(" ");
					ArrayList<String> list = new ArrayList<>();
					Collections.addAll(list, path);
					uploadUtils.delete(list);

					VehicleImgDTO vehicleImgDTO = updateVehicleImg(files);
					vehicleImgDTO.setVehicleId(id);
					data.get().updateVehicleImg(vehicleImgDTO);

					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_INFO_UPDATE);
				})
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_INFO_UPDATE));
	}

	@Transactional
	public ResponseDTO deleteVehicle(Long id) {
		log.info(METHOD_NAME + "- deleteVehicle");

		return Optional.of(new ResponseDTO())
				.filter(u -> id >= 0L)
				.map(v -> vehicleRepository.findById(id))
				.filter(Optional::isPresent)
				.map(v -> vehicleImgRepository.findByVehicleId(id))
				.filter(Optional::isPresent)
				.map(res -> {
					String[] path = res.get().getPath().split(" ");
					ArrayList<String> list = new ArrayList<>();
					Collections.addAll(list, path);
					uploadUtils.delete(list);

					vehicleImgRepository.deleteByVehicleId(id);
					vehicleRepository.deleteById(id);

					return (vehicleRepository.findById(id).isPresent()
							|| vehicleImgRepository.findById(id).isPresent()) ?
							ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_INFO_DELETE) :
							ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_INFO_DELETE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_INFO_DELETE));
	}

	public VehicleImgDTO updateVehicleImg(List<MultipartFile> files) {

		String[] uploadUrl = new String[files.size()];
		ArrayList<String> fileName = new ArrayList<>(), fileType = new ArrayList<>();
		ArrayList<Long> fileLength = new ArrayList<>();
		StringBuilder fileSet = new StringBuilder();

		List<MultipartFile> data = Optional.of(files).filter(v -> !v.isEmpty())
				.map(res -> {
					res.forEach(v -> {
						fileName.add(LocalDateTime.now() + "_" + v.getOriginalFilename());
						fileType.add(v.getContentType());
						fileLength.add(v.getSize());
					});
					return res;
				}).orElseThrow(() -> new ImgFileNotFoundException(ErrorCode.IMG_NOT_FOUND));

		try {
			for (int i = 0; i < files.size(); i++)
				uploadUrl[i] = uploadUtils.getAwsS3().upload(data.get(i), "vehicle/" + fileName.get(i), fileType.get(i), fileLength.get(i));
		} catch (AmazonS3Exception | IOException ae) {
			log.error("차량 이미지 URL 업로드 에러" + METHOD_NAME, ae);
		}
		for (String s : uploadUrl)
			fileSet.append(uploadUtils.getAwsPath()).append(s).append(" ");
		uploadUrl = new String[3];
		uploadUrl[0] = String.valueOf(fileSet);
		fileSet.setLength(0);

		fileType.forEach(res -> fileSet.append(res).append(" "));
		uploadUrl[1] = String.valueOf(fileSet);
		fileSet.setLength(0);

		fileLength.forEach(res -> fileSet.append(res).append(" "));
		uploadUrl[2] = String.valueOf(fileSet);

		return VehicleImgDTO.builder()
				.path(String.valueOf(uploadUrl[0]))
				.type(String.valueOf(uploadUrl[1]))
				.imgSize(String.valueOf(uploadUrl[2])).build();
	}

	/**
	 * 관리자에 의한 프로필 수정, 관리자에 의한 비밀번호 변경은 포함 x
	 */
	@Transactional
	public long update(SignModReqDTO signModReqDTO, long id) throws RuntimeException {

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmpNotFoundException(ErrorCode.EMP_NOT_FOUND));

		long teamId = signModReqDTO.getTeamId();
		long positionId = signModReqDTO.getPositionId();
		if (teamId > teamRepository.findLastTeam() && teamId <= 0)
			new DataIntegrityViolationException(teamId + "가 없습니다.");
		if (positionId > positionRepository.findLastPosition() && teamId <= 0)
			new DataIntegrityViolationException(positionId + "가 없습니다.");

		employee.update(signModReqDTO);
		employeeRepository.updateTP(teamId, positionId, employee.getId());

		return employee.getId();
	}

	/**
	 * 관리자에 의한 비밀번호 변경
	 */
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

	@Transactional
	public List<ReservationResDTO> searchVarious(RoomReservationSearchDTO search) {
		log.info("search : {} , {}, {}, {}", search.getTeamId(), search.getDeptId(), search.getEmpNo(), search.getEmpName());
		List<ReservationResDTO> list = roomQueryDSL.selectByVariousColumns(search).stream().map(roomReservation -> {
			List<List<?>> twoList = serviceMethod.RoomImgListAndRoomObjectList(roomReservation);
			ReservationResDTO reservationResDTO = ReservationResDTO.builder().build().of(roomReservation, (List<RoomObjectResDTO>) twoList.get(0), (List<RoomImgResDTO>) twoList.get(1));
			return reservationResDTO;
		}).collect(Collectors.toList());

		return list;
	}
}
