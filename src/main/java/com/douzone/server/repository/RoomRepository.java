package com.douzone.server.repository;


import com.douzone.server.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<MeetingRoom, Long> {

	@Query("select mr from MeetingRoom mr where mr.id = :id")
	List<MeetingRoom> roomList(@Param("id")long id);

}
