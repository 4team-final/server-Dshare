package com.douzone.server.service;

import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleParseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.dto.vehicle.impl.VehicleWeekTimeDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.douzone.server.config.utils.Msg.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class VehicleService {
	private static final String METHOD_NAME = VehicleService.class.getName();
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final VehicleBookmarkRepository vehicleBookmarkRepository;

	@Transactional
	public ResponseDTO createReservation(VehicleParseDTO vehicleParseDTO, Long empId) {
		log.info(METHOD_NAME + "- createReservation");

		VehicleReservationDTO vehicleReservationDTO = VehicleReservationDTO.builder()
				.vehicleId(vehicleParseDTO.getVehicleId())
				.empId(vehicleParseDTO.getEmpId())
				.reason(vehicleParseDTO.getReason())
				.title(vehicleParseDTO.getTitle())
				.startedAt(LocalDateTime.parse(vehicleParseDTO.getStartedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.endedAt(LocalDateTime.parse(vehicleParseDTO.getEndedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.build();

		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.map(res -> {
					vehicleReservationRepository.save(
							VehicleReservation.builder()
									.vehicle(Vehicle.builder().id((long) vehicleReservationDTO.getVehicleId()).build())
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
	public ResponseDTO createBookmark(Long empId, Long vId) {
		log.info(METHOD_NAME + "- createBookmark");

		return Optional.of(new ResponseDTO())
				.filter(u -> empId > 0)
				.filter(v -> vId > 0)
				.map(v -> {
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
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL, vehicleRepository.findAllReserved()))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllReservedPaging(int pageNum) {
		log.info(METHOD_NAME + "- findAllReservedPaging");

		return Optional.of(new ResponseDTO())
				.map(u -> (pageNum < 0) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_ALL + " 페이지번호 : " + pageNum, vehicleRepository.findAllReservedPaging(PageRequest.of(pageNum, 5))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_ALL + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NONE, vehicleRepository.findAllUnreserved(LocalDateTime.now().plusHours(1L))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NONE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findTypeReserved(String model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		return Optional.of(new ResponseDTO())
				.map(u -> (model == null || model.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_TYPE, vehicleRepository.findTypeReserved(model)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_TYPE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findDateReserved(String start, String end) {
		log.info(METHOD_NAME + "- findDateReserved");

		return Optional.of(new ResponseDTO())
				.map(u -> (start == null || start.equals("") || end == null || end.equals("")) ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_REQUEST_PARAMETER) :
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_DATE, vehicleRepository.findDateReserved(
								LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
								LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_DATE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpBefore(Long id) {
		log.info(METHOD_NAME + "- findEmpBefore");

		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEFORE, vehicleRepository.findEmpBefore(id)) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + FAIL_REQUEST_PARAMETER)
				).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEFORE + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpAfter(Long id) {
		log.info(METHOD_NAME + "- findEmpAfter");

		return Optional.of(new ResponseDTO())
				.map(u -> (id != null) ?
						ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_AFTER, vehicleRepository.findEmpAfter(id)) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + FAIL_REQUEST_PARAMETER)
				).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_AFTER + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_WEEK, vehicleRepository.findWeekVehicle(LocalDateTime.now().minusDays(7L))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_WEEK + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");

		return Optional.of(new ResponseDTO()).map(u -> {
			Map<String, Integer> map = new HashMap<>();
			List<VehicleWeekTimeDTO> result = vehicleRepository.findWeekDate(LocalDateTime.now().minusDays(7L), LocalDateTime.now());
			for (VehicleWeekTimeDTO vehicleWeekTimeDTO : result) {
				String s = vehicleWeekTimeDTO.getSubstring();

				if (!map.containsKey(s)) map.put(s, 1);
				map.put(s, map.get(s) + 1);
			}
			List<Map.Entry<String, Integer>> entry = new LinkedList<>(map.entrySet());
			entry.sort((o1, o2) -> o2.getValue() - o1.getValue());
			return ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_DATE, entry);
		}).orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_DATE + FAIL_FIND_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_RECENT, vehicleRepository.findRecentVehicle()))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_RECENT + FAIL_EXIST_RESULT));
	}

	@Transactional(readOnly = true)
	public ResponseDTO findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_MARK, vehicleBookmarkRepository.findMarkVehicle(empNo)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_MARK + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");

		return Optional.of(new ResponseDTO())
				.map(u -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_BEST_MARK, vehicleBookmarkRepository.findMarkBest(PageRequest.of(0, 3))))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BEST_MARK + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO updateReserved(VehicleParseDTO vehicleParseDTO, Long id) {
		log.info(METHOD_NAME + "- updateReserved");

		VehicleReqDTO vehicleReqDTO = VehicleReqDTO.builder()
				.id(vehicleParseDTO.getId())
				.vehicleId(vehicleParseDTO.getVehicleId())
				.reason(vehicleParseDTO.getReason())
				.title(vehicleParseDTO.getTitle())
				.startedAt(LocalDateTime.parse(vehicleParseDTO.getStartedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.endedAt(LocalDateTime.parse(vehicleParseDTO.getEndedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
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
	public ResponseDTO deleteReserved(Long id, Long empId) {
		log.info(METHOD_NAME + "- deleteReserved");

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
	public ResponseDTO deleteMark(Long id) {
		log.info(METHOD_NAME + "- deleteMark");

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
	public ResponseDTO findVehicleReserved(Long id) {
		log.info(METHOD_NAME + "- findVehicleReserved");

		return Optional.of(new ResponseDTO())
				.filter(u -> (id != null))
				.map(v -> vehicleRepository.findCustom(id))
				.map(res ->
						res.map(iVehicleListResDTO -> ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_FIND_NO, iVehicleListResDTO))
								.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + FAIL_FIND_RESULT)))
				.orElseGet(() -> ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_FIND_NO + FAIL_EXIST_RESULT));
	}

	@Transactional
	public ResponseDTO soonAndIngReservationMyTime(Long empId, int code) {
		log.info(METHOD_NAME + "- soonReservationMyTime");

		return Optional.of(new ResponseDTO())
				.filter(u -> (empId > 0))
				.map(v -> code == 0 ?
						vehicleRepository.ingReservationMyTime(empId, PageRequest.of(0, 1)) :
						vehicleRepository.soonReservationMyTime(empId, PageRequest.of(0, 1)))
				.map(res -> res.get(0).getId() == null ?
						(code == 0 ? ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_ING + FAIL_FIND_RESULT) :
								ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_SOON + FAIL_FIND_RESULT)) :
						(code == 0 ? ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_ING, ChronoUnit.SECONDS.between(LocalDateTime.now(), res.get(0).getTimeTime())) :
								ResponseDTO.of(HttpStatus.OK, SUCCESS_VEHICLE_SOON, ChronoUnit.SECONDS.between(LocalDateTime.now(), res.get(0).getTimeTime()))))
				.orElseGet(() -> code == 0 ?
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_ING) :
						ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_SOON));
	}

}
