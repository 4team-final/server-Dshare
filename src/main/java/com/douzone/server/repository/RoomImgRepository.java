package com.douzone.server.repository;

import com.douzone.server.entity.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {

	@Query("select ri.path from RoomImg ri join fetch MeetingRoom mr on ri.meetingRoom.id = mr.id where mr.id = :roomId")
	List<String> findPathByRoomId(long roomId);

	List<RoomImg> findByMeetingRoom_Id(long roomId);

}
