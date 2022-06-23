package com.douzone.server.repository;

import com.douzone.server.dto.vehicle.*;
import com.douzone.server.dto.vehicle.impl.VehicleWeekTimeDTO;
import com.douzone.server.entity.Vehicle;
import org.springframework.data.domain.Pageable;
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

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, vi.path as vehicleImg, vr.employee.empNo as empNo, vr.employee.name as name " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vi.vehicle.id = vr.vehicle.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findByPaginationReservation(Pageable pageable);

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

	@Query("select substring(vr.startedAt, 12, 2) as substring, vr.id as id, " +
			"vr.startedAt as startedAt, vr.endedAt as endedAt, vr.reason as reason, vr.title as title, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, vr.vehicle as vehicle, vi.path as vehicleImg, " +
			"vr.employee.empNo as empNo, vr.employee.name as name " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vr.vehicle.id = vi.id " +
			"where startedAt > :date and endedAt < :end")
	List<VehicleWeekTimeDTO> weekMostReservedTime(@Param("date") LocalDateTime date, @Param("end") LocalDateTime end);

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

	@Query("select vr.id as id, vr.title as title, vr.startedAt as timeTime " +
			"from VehicleReservation vr " +
			"where vr.employee.id = :empId and vr.startedAt > current_time")
	List<IVehicleTimeResDTO> soonReservationMyTime(@Param("empId") Long empId, Pageable pageable);

	@Query("select vr.id as id, vr.title as title, vr.endedAt as timeTime " +
			"from VehicleReservation vr " +
			"where vr.employee.id = :empId and vr.startedAt < current_time and current_time < vr.endedAt")
	List<IVehicleTimeResDTO> ingReservationMyTime(@Param("empId") Long empId, Pageable pageable);
}
