package com.douzone.server.repository;

import com.douzone.server.dto.vehicle.jpainterface.*;
import com.douzone.server.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, vi.path as vehicleImg, vr.employee.empNo as empNo, vr.employee.name as name " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findByAllReservation();

	@Query(value = "select vr.id as reservationId, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as reservationCreatedAt, vr.modifiedAt as reservationModifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v.name as vName, v.number as vNumber, v.color as color, v.model as model, v.capacity as capacity , vi.path as vehicleImg, e.empNo as empNo, e.name as eName " +
			"from vehicle_reservation vr " +
			"left join vehicle_img vi on vi.vehicleId = vr.vehicleId " +
			"left join vehicle v on v.id = vr.vehicleId " +
			"left join employee e on e.id = vr.empId " +
			"where case when :id > 0 then vr.id <= :id end " +
			"order by vr.id desc limit 15 ", nativeQuery = true)
	List<IVehiclePagingResDTO> findByPaginationReservation(@Param("id") Long id);

	@Query("select distinct v.id as id, v.name as name, v.number as number, " +
			"v.model as model, v.color as color, v.capacity as capacity, vi.path as vehicleImg from Vehicle v " +
			"left outer join VehicleReservation vr on vr.vehicle.id = v.id " +
			"left join fetch VehicleImg vi on v.id = vi.vehicle.id " +
			"where vr.endedAt < current_time or vr.id is null and vr.startedAt > :date")
	List<IVehicleResDTO> findByAllNotReservation(@Param("date") LocalDateTime date);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, vi.path as vehicleImg, vr.employee.empNo as empNo, vr.employee.name as name " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where vr.vehicle.model = :model " +
			"order by id desc ")
	List<IVehicleListResDTO> findByModelReservation(@Param("model") String model);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, vi.path as vehicleImg, vr.employee.empNo as empNo, vr.employee.name as name " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where startedAt between :startDate and :endDate " +
			"order by id desc ")
	List<IVehicleListResDTO> findByDateTimeReservation(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, vr.vehicle as vehicle, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where vr.employee.id = :id and vr.endedAt < current_time  ")
	List<IVehicleEmpResDTO> findByMyPastReservation(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, vr.vehicle as vehicle, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where vr.employee.id = :id and vr.startedAt > current_time  ")
	List<IVehicleEmpResDTO> findByMyCurrentReservation(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, count(vr.vehicle.id) as vcount, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vr.vehicle.id = vi.vehicle.id " +
			"where vr.startedAt > :date  group by vr.vehicle.id order by vcount desc")
	List<IVehicleWeekDTO> weekMostReservedVehicle(@Param("date") LocalDateTime date);

	@Query(value = "select hour(vr.startedAt) as hTime, count(hour(vr.startedAt)) as hCount, vr.id as reservationId, " +
			"vr.startedAt as startedAt, vr.endedAt as endedAt, vr.reason as reason, vr.title as title, " +
			"vr.createdAt as reservationCreatedAt, vr.modifiedAt as reservationModifiedAt, vi.path as vehicleImg, " +
			"v.model as model, v.color as color, v.number as vNumber, v.name as vName, v.capacity as capacity, " +
			"e.empNo as empNo, e.name as eName " +
			"from vehicle_reservation vr " +
			"left join vehicle v on v.id = vr.vehicleId " +
			"left join employee e on e.id = vr.empId " +
			"left join vehicle_img vi on vr.vehicleId = vi.id " +
			"where vr.startedAt > :start and vr.endedAt < :end " +
			"group by hour(vr.startedAt) order by hCount desc ", nativeQuery = true)
	List<IVehicleWeekTimeDTO> weekMostReservedTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("select distinct vr.endedAt as endedAt, v as vehicle, vi.path as vehicleImg " +
			"from Vehicle v " +
			"left join fetch VehicleReservation vr on v.id = vr.vehicle.id " +
			"left join fetch VehicleImg vi on v.id = vi.vehicle.id " +
			"where endedAt is not null order by vr.modifiedAt desc")
	List<IVehicleDateResDTO> findByRecentReservedVehicle();


	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, vi.path as vehicleImg, vr.employee.empNo as empNo, vr.employee.name as name " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where vr.id = :id")
	Optional<IVehicleListResDTO> selectByVehicleReservation(@Param("id") Long id);

	@Query(value = "select vr.id as id, vr.title as title, vr.startedAt as timeTime " +
			"from vehicle_reservation vr " +
			"where vr.empId = :empId and vr.startedAt > current_time limit 1 ", nativeQuery = true)
	Optional<IVehicleTimeResDTO> soonReservationMyTime(@Param("empId") Long empId);

	@Query(value = "select vr.id as id, vr.title as title, vr.endedAt as timeTime " +
			"from vehicle_reservation vr " +
			"where vr.empId = :empId and vr.startedAt < current_time and current_time < vr.endedAt limit 1 ", nativeQuery = true)
	Optional<IVehicleTimeResDTO> ingReservationMyTime(@Param("empId") Long empId);

	@Query(value = "select vr.id as reservationId, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as reservationCreatedAt, vr.modifiedAt as reservationModifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v.name as vName, v.number as vNumber, v.color as color, v.model as model, v.capacity as capacity , vi.path as vehicleImg, " +
			"e.empNo as empNo, e.name as eName " +
			"from vehicle_reservation vr " +
			"left join vehicle_img vi on vi.vehicleId = vr.vehicleId " +
			"left join employee e on e.id = vr.empId " +
			"left join vehicle v on v.id = vr.vehicleId " +
			"where e.id = :id and " +
			"case when :lastId > 0 then vr.id <= :lastId end " +
			"order by vr.id desc limit 15 ", nativeQuery = true)
	List<IVehiclePagingResDTO> findByMyReservationPaging(@Param("lastId") Long lastId, @Param("id") Long id);
}
