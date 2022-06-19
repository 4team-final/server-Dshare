package com.douzone.server.repository;

import com.douzone.server.entity.RoomBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBookmarkRepository extends JpaRepository<RoomBookmark, Long> {
	boolean existsByMeetingRoom_IdAndEmployee_Id(long roomId, long empId);
}
