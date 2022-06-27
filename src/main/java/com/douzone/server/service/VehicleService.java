package com.douzone.server.service;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleParseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.dto.vehicle.VehicleSearchDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import com.douzone.server.repository.querydsl.VehicleQueryDSL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

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

	@Transactional
	public ResponseDTO registerByVehicleReservation(VehicleParseDTO vehicleParseDTO, Long empId) {
		log.info(METHOD_NAME + "- registerByVehicleReservation");
		VehicleReservationDTO vehicleReservationDTO = VehicleReservationDTO.builder()
				.vehicleId(vehicleParseDTO.getVehicleId())
				.empId(vehicleParseDTO.getEmpId())
				.reason(vehicleParseDTO.getReason())
				.title(vehicleParseDTO.getTitle())
				.startedAt(parsing(vehicleParseDTO.getStartedAt()))
				.endedAt(parsing(vehicleParseDTO.getEndedAt()))
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
				.map(res -> vehicleBookmarkRepository.existsByVehicle_IdAndEmployee_Id(vId, empId))
				.map(res -> {
					if (res) return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BOOKMARK_FIND);

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
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL, vehicleRepository.findByAllReservation()))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByPaginationReservation(int pageNum) {
		log.info(METHOD_NAME + "- findByPaginationReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> (pageNum < 0) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL + " 페이지번호 : " + pageNum, vehicleRepository.findByPaginationReservation(PageRequest.of(pageNum, 5))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByAllNotReservation() {
		log.info(METHOD_NAME + "- findByAllNotReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NONE, vehicleRepository.findByAllNotReservation(now().plusHours(1L))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NONE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByModelReservation(String model) {
		log.info(METHOD_NAME + "- findByModelReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> (model == null || model.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_TYPE, vehicleRepository.findByModelReservation(model)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByDateTimeReservation(String start, String end) {
		log.info(METHOD_NAME + "- findByDateTimeReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> (start == null || start.equals("") || end == null || end.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_DATE, vehicleRepository.findByDateTimeReservation(
								parsing(start),
								parsing(end))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyPastReservation(Long id) {
		log.info(METHOD_NAME + "- findByMyPastReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEFORE, vehicleRepository.findByMyPastReservation(id)) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + FAIL_REQUEST_PARAMETER)
				).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyCurrentReservation(Long id) {
		log.info(METHOD_NAME + "- findByMyCurrentReservation");
		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_AFTER, vehicleRepository.findByMyCurrentReservation(id)) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + FAIL_REQUEST_PARAMETER)
				).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO weekMostReservedVehicle() {
		log.info(METHOD_NAME + "- weekMostReservedVehicle");
		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_WEEK, vehicleRepository.weekMostReservedVehicle(now().minusDays(7L))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_WEEK + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO weekMostReservedTime() {
		log.info(METHOD_NAME + "- weekMostReservedTime");
		return Optional.of(new ResponseDTO())
				.map(u -> vehicleRepository.weekMostReservedTime(now().minusDays(7L), now()))
				.map(res -> res.size() > 0 ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, res) :
						ResponseDTO.fail(HttpStatus.OK, FAIL_VEHICLE_BEST_DATE))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + FAIL_FIND_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByRecentReservedVehicle() {
		log.info(METHOD_NAME + "- findByRecentReservedVehicle");
		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RECENT, vehicleRepository.findByRecentReservedVehicle()))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RECENT + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findByMyBookMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_MARK, vehicleBookmarkRepository.findByMyBookMarkVehicle(empNo)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_MARK + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO selectByBookMarkTop3Vehicle() {
		log.info(METHOD_NAME + "- selectByBookMarkTop3Vehicle");
		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_MARK, vehicleBookmarkRepository.selectByBookMarkTop3Vehicle(PageRequest.of(0, 3))))
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
				.startedAt(parsing(vehicleParseDTO.getStartedAt()))
				.endedAt(parsing(vehicleParseDTO.getEndedAt()))
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
				.map(res -> res.isPresent() ? res.get().getEmployee().getId() : -1L)
				.filter(v -> v.equals(empId))
				.map(v -> {
					vehicleReservationRepository.deleteById(id);
					return (vehicleReservationRepository.findById(id).isPresent()) ?
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
				.map(v -> vehicleRepository.selectByVehicleReservation(id))
				.map(res ->
						res.map(iVehicleListResDTO -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NO, iVehicleListResDTO))
								.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + FAIL_FIND_RESULT)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO soonReservationMyTime(Long empId) {
		log.info(METHOD_NAME + "- soonReservationMyTime");
		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(v -> vehicleRepository.soonReservationMyTime(empId))
				.map(res -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_SOON, ChronoUnit.SECONDS.between(now(), res.getTimeTime())))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_SOON));
	}

	@Transactional
	public ResponseDTO ingReservationMyTime(Long empId) {
		log.info(METHOD_NAME + "- ingReservationMyTime");
		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(v -> vehicleRepository.ingReservationMyTime(empId))
				.map(res -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_ING, ChronoUnit.SECONDS.between(now(), res.getTimeTime())))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_ING));
	}

	public LocalDateTime now() {
		return LocalDateTime.now();
	}

	public LocalDateTime parsing(String time) {
		return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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
							.endedAt(now())
							.build());
					return ResponseDTO.of(HttpStatus.OK, SUCCESS_MOVE_UP_RESERVATION);
				}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_MOVE_UP_RESERVATION));
	}

	@Transactional(readOnly = true)
	public ResponseDTO selectByVariousColumns(VehicleSearchDTO vehicleSearchDTO) {
		log.info(METHOD_NAME + "- selectByVariousColumns");
		return Optional.of(new ResponseDTO())
				.map(v -> vehicleQueryDSL.selectByVariousColumns(vehicleSearchDTO))
				.map(v -> v.isEmpty() ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_VARIOUS_COLUMNS + FAIL_EXIST_RESULT) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_SELECT_VARIOUS_COLUMNS, v))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_SELECT_VARIOUS_COLUMNS + FAIL_FIND_RESULT));
	}
}
