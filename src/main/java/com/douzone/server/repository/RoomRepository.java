package com.douzone.server.repository;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<MeetingRoom, Long> {


}
