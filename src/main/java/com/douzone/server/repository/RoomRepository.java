package com.douzone.server.repository;

import com.douzone.server.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<MeetingRoom, Long> {
}
