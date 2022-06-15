package com.douzone.server.service;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleReqDTO;
import com.douzone.server.dto.vehicle.VehicleReservationDTO;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Vehicle;
import com.douzone.server.entity.VehicleBookmark;
import com.douzone.server.entity.VehicleReservation;
import com.douzone.server.repository.EmployeeRepository;
import com.douzone.server.repository.VehicleBookmarkRepository;
import com.douzone.server.repository.VehicleRepository;
import com.douzone.server.repository.VehicleReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class VehicleService {
	private static final String METHOD_NAME = VehicleService.class.getName();
	private final VehicleRepository vehicleRepository;
	private final VehicleReservationRepository vehicleReservationRepository;
	private final VehicleBookmarkRepository vehicleBookmarkRepository;
	private final EmployeeRepository employeeRepository;

	@Transactional
	public ResponseDTO createReservation(VehicleReservationDTO vehicleReservationDTO, Long empId,Long vId) {
		log.info(METHOD_NAME + "-createReservation");
		try {
			VehicleReservation vehicleReservation =
					VehicleReservation.builder()
					.vehicle(Vehicle.builder().id(vId).build())
					.employee(Employee.builder().id(empId).build())
					.reason(vehicleReservationDTO.getReason())
					.title(vehicleReservationDTO.getTitle())
							.startedAt(vehicleReservationDTO.getStartedAt())
							.endedAt(vehicleReservationDTO.getEndedAt())
					.build();

			vehicleReservationRepository.save(vehicleReservation);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_RESERVE);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_RESERVE);
	}

	@Transactional
	public ResponseDTO createBookmark(Long empId, Long vId) {
		log.info(METHOD_NAME + "-createBookmark");

		try {
			VehicleBookmark vehicleBookmark =
					VehicleBookmark.builder()
							.vehicle(Vehicle.builder().id(vId).build())
							.employee(Employee.builder().id(empId).build())
							.build();

			vehicleBookmarkRepository.save(vehicleBookmark);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BOOKMARK);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BOOKMARK);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllReserved() {
		log.info(METHOD_NAME + "- findAllReserved");
		try {
			List<Object[]> list = vehicleReservationRepository.findAllReserved();

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_ALL);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_ALL, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_ALL);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findAllUnreserved() {
		log.info(METHOD_NAME + "- findAllUnreserved");
		try {
			List<Vehicle> list = vehicleRepository.findAllUnreserved();

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_NONE);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_NONE, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_NONE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findTypeReserved(String model) {
		log.info(METHOD_NAME + "- findTypeReserved");
		try {
			List<Vehicle> list = vehicleRepository.findTypeReserved(model);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_TYPE);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_TYPE, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_TYPE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findDateReserved(Date date) {
		log.info(METHOD_NAME + "- findTypeReserved");
		try {
			List<Vehicle> list = vehicleRepository.findDateReserved(date);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_DATE);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_DATE, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_DATE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpBefore(Long id, Date date) {
		log.info(METHOD_NAME + "- findEmpBefore");
		try {
			List<VehicleReservation> list = vehicleReservationRepository.findEmpBefore(id, date);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEFORE);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEFORE, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEFORE);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findEmpAfter(Long id, Date date) {
		log.info(METHOD_NAME + "- findEmpAfter");
		try {
			List<VehicleReservation> list = vehicleReservationRepository.findEmpAfter(id, date);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_AFTER);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_AFTER, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_AFTER);
	}

	@Transactional
	public ResponseDTO findWeekVehicle() {
		log.info(METHOD_NAME + "- findWeekVehicle");
		try {
			Vehicle vehicle = vehicleRepository.findWeekVehicle();

			if (vehicle == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEST_WEEK);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEST_WEEK, vehicle);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEST_WEEK);
	}

	@Transactional
	public ResponseDTO findWeekDate() {
		log.info(METHOD_NAME + "- findWeekDate");
		try {
			Integer result = vehicleRepository.findWeekDate();

			if (result == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEST_DATE);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEST_DATE, result);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEST_DATE);
	}

	@Transactional
	public ResponseDTO findRecentVehicle() {
		log.info(METHOD_NAME + "- findRecentVehicle");
		try {
			Vehicle vehicle = vehicleRepository.findRecentVehicle();

			if (vehicle == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_RECENT);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_RECENT, vehicle);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_RECENT);
	}

	@Transactional(readOnly = true)
	public ResponseDTO findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "- findMarkVehicle");
		try {
			List<Vehicle> list = vehicleRepository.findMarkVehicle(empNo);

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_FIND_MARK);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_FIND_MARK, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_FIND_MARK);
	}

	@Transactional
	public ResponseDTO findMarkBest() {
		log.info(METHOD_NAME + "- findMarkBest");
		try {
			List<Vehicle> list = vehicleRepository.findMarkBest();

			if (list == null)
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_BEST_MARK);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_BEST_MARK, list);
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_BEST_MARK);
	}

	@Transactional
	public ResponseDTO updateReserved(VehicleReqDTO vehicleReqDTO) {
		log.info(METHOD_NAME + "- updateReserved");
		try {
			Optional<VehicleReservation> data = vehicleReservationRepository.findById(vehicleReqDTO.getId());
			if (data.isPresent()) {
				VehicleReservation vehicleReservation = data.get();
				vehicleReservation.updateReserved(vehicleReqDTO);

				return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_UPDATE);
			}
			return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_UPDATE);
		} catch (NoSuchElementException ne) {
			log.error("값이 들어갈 공간이 없습니다." + ne);
		} catch (Exception e) {
			log.error("SERVER ERROR" + e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_UPDATE);
	}

	@Transactional
	public ResponseDTO deleteReserved(Long id) {
		log.info(METHOD_NAME + "- deleteReserved");
		try {
			vehicleReservationRepository.deleteById(id);

			if (vehicleReservationRepository.findById(id).isPresent())
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_DELETE);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_DELETE);
		} catch (Exception e) {
			log.error("SERVER ERROR" + e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_DELETE);
	}

	@Transactional
	public ResponseDTO deleteMark(Long id) {
		log.info(METHOD_NAME + "- deleteMark");
		try {
			vehicleBookmarkRepository.deleteById(id);

			if (vehicleBookmarkRepository.findById(id).isPresent())
				return ResponseDTO.fail(HttpStatus.BAD_REQUEST, Msg.FAIL_VEHICLE_DELETE_MARK);

			return ResponseDTO.of(HttpStatus.OK, Msg.SUCCESS_VEHICLE_DELETE_MARK);
		} catch (Exception e) {
			log.error("SERVER ERROR" + e);
		}
		return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR, Msg.FAIL_VEHICLE_DELETE_MARK);
	}
}
