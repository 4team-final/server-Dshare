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
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findAllReserved();

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where startedAt > current_time " +
			"order by id desc")
	List<IVehicleListResDTO> findAllReservedPaging(Pageable pageable);

	@Query("select distinct v.id as id, v.name as name, v.number as number, " +
			"v.model as model, v.color as color, v.capacity as capacity, vi.path as vehicleImg from Vehicle v " +
			"left outer join VehicleReservation vr on vr.vehicle.id = v.id " +
			"left join fetch VehicleImg vi on v.id = vi.vehicle.id " +
			"where vr.endedAt < current_time or vr.id is null and vr.startedAt > :date")
	List<IVehicleResDTO> findAllUnreserved(@Param("date") LocalDateTime date);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where v.model = :model " +
			"order by id desc ")
	List<IVehicleListResDTO> findTypeReserved(@Param("model") String model);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where startedAt between :startDate and :endDate " +
			"order by id desc ")
	List<IVehicleListResDTO> findDateReserved(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, v as vehicle, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where e.id = :id and vr.endedAt < current_time  ")
	List<IVehicleEmpResDTO> findEmpBefore(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, v as vehicle, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where e.id = :id and vr.startedAt > current_time  ")
	List<IVehicleEmpResDTO> findEmpAfter(@Param("id") Long id);

	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, vr.reason as reason, vr.title as title, " +
			"vr.vehicle as vehicle, count(vr.vehicle.id) as vcount, vi.path as vehicleImg " +
			"from VehicleReservation vr " +
			"left join fetch VehicleImg vi on vr.vehicle.id = vi.vehicle.id " +
			"where vr.startedAt > :date  group by vr.vehicle.id order by vcount desc")
	List<IVehicleWeekDTO> findWeekVehicle(@Param("date") LocalDateTime date);

	@Query("select substring(vr.startedAt, 12, 2) as substring, vr.id as id, " +
			"vr.startedAt as startedAt, vr.endedAt as endedAt, vr.reason as reason, vr.title as title, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, v as vehicle, vi.path as vehicleImg, " +
			"e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on v.id = vi.id " +
			"where startedAt > :date and endedAt < :end")
	List<VehicleWeekTimeDTO> findWeekDate(@Param("date") LocalDateTime date, @Param("end") LocalDateTime end);

	@Query("select distinct vr.endedAt as endedAt, v as vehicle, vi.path as vehicleImg " +
			"from Vehicle v " +
			"left join fetch VehicleReservation vr on v.id = vr.vehicle.id " +
			"left join fetch VehicleImg vi on v.id = vi.vehicle.id " +
			"where endedAt is not null order by vr.modifiedAt desc")
	List<IVehicleDateResDTO> findRecentVehicle();


	@Query("select vr.id as id, vr.startedAt as startedAt, vr.endedAt as endedAt, " +
			"vr.createdAt as createdAt, vr.modifiedAt as modifiedAt, " +
			"vr.reason as reason, vr.title as title, " +
			"v as vehicle, vi.path as vehicleImg, e.empNo as empNo, e.name as name " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"left join fetch VehicleImg vi on vi.vehicle.id = v.id " +
			"where vr.id = :id")
	Optional<IVehicleListResDTO> findCustom(@Param("id") Long id);

	@Query("select vr.id as id, vr.title as title, vr.startedAt as timeTime " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :empId and vr.startedAt > current_time")
	List<IVehicleTimeResDTO> soonReservationMyTime(@Param("empId") Long empId, Pageable pageable);

	@Query("select vr.id as id, vr.title as title, vr.endedAt as timeTime " +
			"from VehicleReservation vr " +
			"left join fetch Vehicle v on vr.vehicle.id = v.id " +
			"left join fetch Employee e on vr.employee.id = e.id " +
			"where e.id = :empId and vr.startedAt < current_time and current_time < vr.endedAt")
	List<IVehicleTimeResDTO> ingReservationMyTime(@Param("empId") Long empId, Pageable pageable);
}
