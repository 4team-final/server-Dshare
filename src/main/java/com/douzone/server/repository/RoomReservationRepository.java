package com.douzone.server.repository;

import com.douzone.server.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

	List<RoomReservation> findByMeetingRoom_Id(Long roomId);

	boolean existsByEmployee_Id(Long empId);

	@Modifying
	@Query(value = "update room_reservation set modifiedAt = :modifiedAt ,createdAt = :createdAt where id= :id", nativeQuery = true)
	void TestUpdateModified( @Param("modifiedAt")LocalDateTime modifiedAt, @Param("createdAt")LocalDateTime createdAt,@Param("id") Long id);

	@Query(value = "select rr.startedAt from room_reservation rr where id= :id", nativeQuery = true)
	String TestStartedAt(@Param("id")Long id);


}
