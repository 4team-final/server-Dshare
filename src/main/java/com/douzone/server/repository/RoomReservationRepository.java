package com.douzone.server.repository;

import com.douzone.server.entity.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

	List<RoomReservation> findByMeetingRoom_Id(Long roomId);

}
