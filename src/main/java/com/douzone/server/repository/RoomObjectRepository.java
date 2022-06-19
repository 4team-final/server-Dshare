package com.douzone.server.repository;

import com.douzone.server.entity.RoomObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomObjectRepository extends JpaRepository<RoomObject, Long> {

	List<RoomObject> findByMeetingRoom_Id(long roomId);
	
}
