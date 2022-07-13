package com.douzone.server.service;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.*;
import com.douzone.server.dto.vehicle.impl.VehicleListResDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import com.douzone.server.repository.querydsl.VehicleQueryDSL;
import com.douzone.server.service.method.VehicleServiceMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.douzone.server.config.utils.Msg.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleService {
	private static final String METHOD_NAME = VehicleService.class.getName();
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final VehicleBookmarkRepository vehicleBookmarkRepository;
	private final VehicleQueryDSL vehicleQueryDSL;

	private final VehicleServiceMethod vehicleServiceMethod;

	@Transactional
	public ResponseDTO registerByVehicleReservation(VehicleParseDTO vehicleParseDTO, Long empId) {
		log.info(METHOD_NAME + "- registerByVehicleReservation");
		VehicleReservationDTO vehicleReservationDTO = VehicleReservationDTO.builder()
				.vehicleId(vehicleParseDTO.getVehicleId())
				.empId(vehicleParseDTO.getEmpId())
				.reason(vehicleParseDTO.getReason())
				.title(vehicleParseDTO.getTitle())
				.startedAt(vehicleServiceMethod.parsing(vehicleParseDTO.getStartedAt()))
				.endedAt(vehicleServiceMethod.parsing(vehicleParseDTO.getEndedAt()))
				.build();
		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(res -> {
					vehicleReservationRepository.save(
							VehicleReservation.builder()
									.vehicle(Vehicle.builder().id(vehicleReservationDTO.getVehicleId()).build())
									.employee(Employee.builder().id(empId).build())
									.reason(vehicleReservationDTO.getReason())
									.title(vehicleReservationDTO.getTitle())
									.startedAt(vehicleReservationDTO.getStartedAt())
									.endedAt(vehicleReservationDTO.getEndedAt())
									.build()
					);
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RESERVE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RESERVE));
	}

	@Transactional
	public ResponseDTO registerByVehicleBookmark(Long empId, Long vId) {
		log.info(METHOD_NAME + "- registerByVehicleBookmark");
		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.filter(v -> vId > 0)
				.map(res -> {
					vehicleBookmarkRepository.save(
							VehicleBookmark.builder()
									.vehicle(Vehicle.builder().id(vId).build())
									.employee(Employee.builder().id(empId).build())
									.build()
					);
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BOOKMARK);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BOOKMARK));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByAllReservation() {
		log.info(METHOD_NAME + "- findByAllReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToList(vehicleRepository.findByAllReservation()))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByPaginationReservation(Long lastId) {
		log.info(METHOD_NAME + "- findByPaginationReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToPaging(vehicleRepository.findByPaginationReservation(lastId)))
				.map(u -> (lastId < 0) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL + " 페이지번호 : " + lastId, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByPaginationReservation2(Long page) {
		log.info(METHOD_NAME + "- findByPaginationReservation");
		Long page2 = (page - 1) * 10;
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToPaging(vehicleRepository.findByPaginationReservation2(page2)))
				.map(u -> (page < 0) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL + " 페이지번호 : " + page, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}


	@Transactional(readOnly = true)
	public ResponseDTO findByAllNotReservation() {
		log.info(METHOD_NAME + "- findByAllNotReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToVehicle(vehicleRepository.findByAllNotReservation(vehicleServiceMethod.now().plusHours(1L))))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NONE, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NONE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByModelReservation(String model) {
		log.info(METHOD_NAME + "- findByModelReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToList(vehicleRepository.findByModelReservation(model)))
				.map(u -> (model == null || model.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_TYPE, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByDateTimeReservation(String start, String end) {
		log.info(METHOD_NAME + "- findByDateTimeReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> {
					if (start == null || start.equals("") || end == null || end.equals("")) {
						return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_REQUEST_PARAMETER);
					}
					return v;
				})
				.map(v -> vehicleServiceMethod.convertToList(vehicleRepository.findByDateTimeReservation(LocalDateTime.parse(start), LocalDateTime.parse(end))))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_DATE, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByDateTimeReservation2(String start, String end) {
		log.info(METHOD_NAME + "- findByDateTimeReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> {
					if (start == null || start.equals("") || end == null || end.equals("")) {
						return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_REQUEST_PARAMETER);
					}
					return v;
				})
				.map(v -> vehicleRepository.findByDateTimeReservation2(LocalDateTime.parse(start), LocalDateTime.parse(end)))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_DATE, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyPastReservation(Long id) {
		log.info(METHOD_NAME + "- findByMyPastReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> {
					if (id != null) {
						return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + FAIL_REQUEST_PARAMETER);
					}
					return v;
				})
				.map(v -> vehicleServiceMethod.convertToEmp(vehicleRepository.findByMyPastReservation(id)))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEFORE, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyCurrentReservation(Long id) {
		log.info(METHOD_NAME + "- findByMyCurrentReservation");
		return Optional.of(new ResponseDTO())
				.map(v -> {
					if (id != null) {
						return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + FAIL_REQUEST_PARAMETER);
					}
					return v;
				})
				.map(v -> vehicleServiceMethod.convertToEmp(vehicleRepository.findByMyCurrentReservation(id)))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_AFTER, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyReservation(Long id) {
		log.info(METHOD_NAME + "- findByMyReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_SELECT_MY_VEHICLE, MyVehicleReservationDTO.builder()
								.beforeList(vehicleServiceMethod.convertToEmp(vehicleRepository.findByMyPastReservation(id)))
								.afterList(vehicleServiceMethod.convertToEmp(vehicleRepository.findByMyCurrentReservation(id)))
								.build()) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_MY_VEHICLE + FAIL_REQUEST_PARAMETER))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_MY_VEHICLE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyReservationPaging(Long lastId, Long id) {
		log.info(METHOD_NAME + "- findByMyReservationPaging");
		return Optional.of(new ResponseDTO())
				.filter(v -> id != null)
				.map(v -> vehicleServiceMethod.convertToPaging(vehicleRepository.findByMyReservationPaging(lastId, id)))
				.map(u -> (lastId < 0) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_MY_VEHICLE + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_SELECT_MY_VEHICLE, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_MY_VEHICLE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO weekMostReservedVehicle(Long datetime) {
		log.info(METHOD_NAME + "- weekMostReservedVehicle");
		LocalDateTime now = LocalDateTime.now();
		return Optional.of(new ResponseDTO())
				.map(v ->
						vehicleServiceMethod.convertToWeek(vehicleRepository.weekMostReservedVehicle(now.minusDays(datetime), now)))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_WEEK, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_WEEK + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO weekMostReservedTime(Long datetime) {
		log.info(METHOD_NAME + "- weekMostReservedTime");
		LocalDateTime now = LocalDateTime.now();
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToWeekTime(vehicleRepository.weekMostReservedTime(now.minusDays(datetime), now)))
				.map(u -> u.size() > 0 ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, u) :
						ResponseDTO.fail(HttpStatus.OK, FAIL_VEHICLE_BEST_DATE))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + FAIL_FIND_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO weekstartMostReservedTime(Long datetime) {
		log.info(METHOD_NAME + "- weekstartMostReservedTime");
		LocalDateTime now = LocalDateTime.now();
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToWeekTime(vehicleRepository.weekstartMostReservedTime(now.minusDays(datetime), now)))
				.map(u -> u.size() > 0 ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, u) :
						ResponseDTO.fail(HttpStatus.OK, FAIL_VEHICLE_BEST_DATE))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + FAIL_FIND_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByRecentReservedVehicle() {
		log.info(METHOD_NAME + "- findByRecentReservedVehicle");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToDate(vehicleRepository.findByRecentReservedVehicle(LocalDateTime.now()).stream().limit(3).collect(Collectors.toList())))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RECENT, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RECENT + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyBookMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		return Optional.of(new ResponseDTO())
				.map(v -> {
					List<VehicleBookMarkDTO> vList = new ArrayList<>();
					List<VehicleBookmark> list = vehicleBookmarkRepository.findByMyBookMarkVehicle(empNo);
					for (VehicleBookmark vehicleBookmark : list) {
						List<String> imgList = vehicleServiceMethod.setPathToList(vehicleBookmark.getVehicle().getId());
						vList.add(new VehicleBookMarkDTO().of(Vehicle.builder()
										.id(vehicleBookmark.getVehicle().getId())
										.name(vehicleBookmark.getVehicle().getName())
										.color(vehicleBookmark.getVehicle().getColor())
										.capacity(vehicleBookmark.getVehicle().getCapacity())
										.number(vehicleBookmark.getVehicle().getNumber())
										.model(vehicleBookmark.getVehicle().getModel())
										.build(),
								imgList,
								vehicleBookmark.getId()));
					}
					return vList;
				})
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_MARK, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_MARK + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO selectByBookMarkTop3Vehicle() {
		log.info(METHOD_NAME + "- selectByBookMarkTop3Vehicle");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToVehicleOne(vehicleBookmarkRepository.selectByBookMarkTop3Vehicle(PageRequest.of(0, 3))))
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_MARK, u))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_MARK + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO updateByMyReservation(VehicleParseDTO vehicleParseDTO, Long id) {
		log.info(METHOD_NAME + "- updateByMyReservation");
		VehicleReqDTO vehicleReqDTO = VehicleReqDTO.builder()
				.id(vehicleParseDTO.getId())
				.vehicleId(vehicleParseDTO.getVehicleId())
				.reason(vehicleParseDTO.getReason())
				.title(vehicleParseDTO.getTitle())
				.startedAt(vehicleServiceMethod.parsing(vehicleParseDTO.getStartedAt()))
				.endedAt(vehicleServiceMethod.parsing(vehicleParseDTO.getEndedAt()))
				.build();

		return Optional.of(new ResponseDTO())
				.map(u -> Optional.ofNullable(vehicleReqDTO).map(VehicleReqDTO::getId))
				.filter(Optional::isPresent)
				.map(v -> vehicleReservationRepository.findById(vehicleReqDTO.getId()))
				.filter(Optional::isPresent)
				.filter(res -> Objects.equals(res.get().getEmployee().getId(), id))
				.map(ans -> {
					ans.get().updateReserved(vehicleReqDTO);
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_UPDATE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_UPDATE + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO deleteByMyReservation(Long id, Long empId) {
		log.info(METHOD_NAME + "- deleteByMyReservation");
		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(u -> vehicleReservationRepository.findById(id))
				.filter(Optional::isPresent)
				.map(res -> {
					vehicleServiceMethod.convertToTimeAndResetIsSeat(
							res.get().getStartedAt(),
							res.get().getEndedAt(),
							res.get().getVehicle().getId(),
							res.get().getEmployee().getEmpNo());
					return res.get().getId();
				})
				.map(v -> {
					vehicleReservationRepository.deleteById(v);
					return (vehicleReservationRepository.findById(v).isPresent()) ?
							ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE + FAIL_FIND_RESULT) :
							ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_DELETE);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO deleteByMyBookMark(Long id) {
		log.info(METHOD_NAME + "- deleteByMyBookMark");
		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(v -> {
					vehicleBookmarkRepository.deleteById(id);
					return (vehicleBookmarkRepository.findById(id).isPresent()) ?
							ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE_MARK + FAIL_FIND_RESULT) :
							ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_DELETE_MARK);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_DELETE_MARK + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO selectByVehicleReservation(Long id) {
		log.info(METHOD_NAME + "- selectByVehicleReservation");
		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(u -> vehicleRepository.selectByVehicleReservation(id))
				.filter(Optional::isPresent)
				.map(v -> new VehicleListResDTO().of(v.get(), vehicleServiceMethod.setPathToList(v.get().getVehicle().getId())))
				.map(res -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NO, res))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO soonReservationMyTime(Long empId) {
		log.info(METHOD_NAME + "- soonReservationMyTime");
		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(v -> vehicleRepository.soonReservationMyTime(empId))
				.map(res -> res.map(iVehicleTimeResDTO ->
								ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_SOON, ChronoUnit.SECONDS.between(vehicleServiceMethod.now(), iVehicleTimeResDTO.getTimeTime())))
						.orElseGet(() -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_NOT_SOON)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_SOON));
	}

	@Transactional
	public ResponseDTO ingReservationMyTime(Long empId) {
		log.info(METHOD_NAME + "- ingReservationMyTime");
		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(v -> vehicleRepository.ingReservationMyTime(empId))
				.map(res -> res.map(iVehicleTimeResDTO ->
								ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_ING, ChronoUnit.SECONDS.between(vehicleServiceMethod.now(), iVehicleTimeResDTO.getTimeTime())))
						.orElseGet(() -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_NOT_ING)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_ING));
	}

	@Transactional
	public ResponseDTO earlyReturnOfVehicle(Long id, Long empId) {
		log.info(METHOD_NAME + "- earlyReturnOfVehicle");
		return Optional.of(new ResponseDTO())
				.filter(u -> (id > 0))
				.map(u -> vehicleReservationRepository.findById(id))
				.filter(Optional::isPresent)
				.filter(v -> Objects.equals(v.get().getEmployee().getId(), empId))
				.map(v -> {
					v.get().updateReserved(VehicleReqDTO.builder()
							.vehicleId(v.get().getVehicle().getId())
							.reason(v.get().getReason())
							.title(v.get().getTitle())
							.startedAt(v.get().getStartedAt())
							.endedAt(vehicleServiceMethod.now())
							.build());
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_MOVE_UP_RESERVATION);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_MOVE_UP_RESERVATION));
	}

	@Transactional(readOnly = true)
	public ResponseDTO selectByVariousColumns(VehicleSearchDTO vehicleSearchDTO) {
		log.info(METHOD_NAME + "- selectByVariousColumns");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleQueryDSL.selectByVariousColumns(vehicleSearchDTO))
				.map(v -> {
					for (VehicleVariousDTO vehicleVariousDTO : v) {
						vehicleVariousDTO.setImgList(vehicleServiceMethod.setPathToList(vehicleVariousDTO.getVId()));
					}
					return v;
				})
				.map(v -> v.isEmpty() ?
						ResponseDTO.fail(HttpStatus.OK, FAIL_SELECT_VARIOUS_COLUMNS + FAIL_EXIST_RESULT) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_SELECT_VARIOUS_COLUMNS, v))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_VARIOUS_COLUMNS + FAIL_FIND_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO selectByVariousColumns(VehicleSearchDTO vehicleSearchDTO, long page) {
		log.info(METHOD_NAME + "- selectByVariousColumns");
		long total = vehicleQueryDSL.countReservation(vehicleSearchDTO);
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleQueryDSL.selectByVariousColumns(vehicleSearchDTO, page))
				.map(v -> {
					for (VehicleVariousDTO vehicleVariousDTO : v) {
						vehicleVariousDTO.setImgList(vehicleServiceMethod.setPathToList(vehicleVariousDTO.getVId()));
						vehicleVariousDTO.setTotal(total);
					}
					return v;
				})
				.map(v -> v.isEmpty() ?
						ResponseDTO.fail(HttpStatus.OK, FAIL_SELECT_VARIOUS_COLUMNS + FAIL_EXIST_RESULT) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_SELECT_VARIOUS_COLUMNS, v))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_VARIOUS_COLUMNS + FAIL_FIND_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO selectByAllVehicle() {
		log.info(METHOD_NAME, "- selectByAllVehicle");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleServiceMethod.convertToVehicle(vehicleRepository.selectByAllVehicle()))
				.filter(res -> res.size() > 0)
				.map(res -> ResponseDTO.of(HttpStatus.OK, SUCCESS_SELECT_VEHICLE_ALL, res))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_VEHICLE_ALL));
	}
}
