package com.douzone.server.service;

import com.douzone.server.config.utils.Msg;
import com.douzone.server.config.utils.ResponseDTO;
import com.douzone.server.dto.vehicle.VehicleResDTO;
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
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

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

	public VehicleResDTO findAllReserved() {
		log.info(METHOD_NAME + "-findAllReserved");
		try {
			List<VehicleReservation> list = vehicleReservationRepository.findAllReserved();

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findAllUnreserved() {
		log.info(METHOD_NAME + "-findAllUnreserved");
		try {
			List<Vehicle> list = vehicleRepository.findAllUnreserved();

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findTypeReserved(String model) {
		log.info(METHOD_NAME + "-findTypeReserved");
		try {
			List<Vehicle> list = vehicleRepository.findTypeReserved(model);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findDateReserved(Date date) {
		log.info(METHOD_NAME + "-findTypeReserved");
		try {
			List<Vehicle> list = vehicleRepository.findDateReserved(date);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findEmpBefore(Long id, Date date) {
		log.info(METHOD_NAME + "-findEmpBefore");
		try {
			List<VehicleReservation> list = vehicleRepository.findEmpBefore(id, date);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findEmpAfter(Long id, Date date) {
		log.info(METHOD_NAME + "-findEmpAfter");
		try {
			List<VehicleReservation> list = vehicleRepository.findEmpAfter(id, date);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findWeekVehicle() {
		log.info(METHOD_NAME + "-findWeekVehicle");
		try {
			Vehicle vehicle = vehicleRepository.findWeekVehicle();

			if (vehicle == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(vehicle).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findWeekDate() {
		log.info(METHOD_NAME + "-findWeekDate");
		try {
			Integer result = vehicleRepository.findWeekDate();

			if (result == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(result).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findRecentVehicle() {
		log.info(METHOD_NAME + "-findRecentVehicle");
		try {
			Vehicle vehicle = vehicleRepository.findRecentVehicle();

			if (vehicle == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(vehicle).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findMarkVehicle(String empNo) {
		log.info(METHOD_NAME + "-findMarkVehicle");
		try {
			List<Vehicle> list = vehicleRepository.findMarkVehicle(empNo);

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public VehicleResDTO findMarkBest() {
		log.info(METHOD_NAME + "-findMarkBest");
		try {
			List<Vehicle> list = vehicleRepository.findMarkBest();

			if (list == null) return VehicleResDTO.builder().code(1).build();

			return VehicleResDTO.builder().code(0).data(list).build();
		} catch (Exception e) {
			log.error("SERVER ERROR", e);
		}
		return VehicleResDTO.builder().code(2).build();
	}

	public ResponseDTO deleteReserved(Long id) {
		log.info(METHOD_NAME + "-deleteReserved");
		vehicleReservationRepository.deleteById(id);
		return null;
	}

	public ResponseDTO deleteBookmark(Long id) {
		log.info(METHOD_NAME + "-deleteBookmark");
		vehicleBookmarkRepository.deleteById(id);
		return null;
	}

//			log.info(METHOD_NAME + "-createBookmark");
//
//		try {
//		VehicleBookmark vehicleBookmark = VehicleBookmark.builder()
//				.employee(Employee.builder().id(1L).build())
//				.vehicle(Vehicle.builder().id(1L).build())
//				.build();
//		VehicleBookmark result = vehicleBookmarkRepository.save(vehicleBookmark);
//		if (result.getId() == null) return ResponseDTO.fail(HttpStatus.BAD_REQUEST, FAIL_VEHICLE_BOOKMARK);
//		return new ResponseDTO().of(HttpStatus.OK, SUCCESS_VEHICLE_BOOKMARK);
//	} catch (Exception e) {
//		log.error("SERVER ERROR", e);
//	}
//		return null;
}
