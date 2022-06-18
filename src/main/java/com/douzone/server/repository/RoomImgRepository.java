package com.douzone.server.repository;

import com.douzone.server.entity.RoomImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomImgRepository extends JpaRepository<RoomImg, Long> {
}
