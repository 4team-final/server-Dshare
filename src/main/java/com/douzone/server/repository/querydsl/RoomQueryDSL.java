package com.douzone.server.repository.querydsl;
import com.douzone.server.dto.room.QRoomBookmarkDTO;
import com.douzone.server.dto.room.RoomBookmarkDTO;
import com.douzone.server.entity.*;
import com.querydsl.core.Tuple;
import com.douzone.server.entity.Employee;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QPosition.position;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QTeam.team;
import static com.douzone.server.entity.QRoomBookmark.roomBookmark;

@Repository
@RequiredArgsConstructor
public class RoomQueryDSL {
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 전체 회의실 예약 조회
     * @return
     */
    public List<RoomReservation> selectAllReservation() {
        return jpaQueryFactory
                .select(roomReservation)
                .from(roomReservation)
                .join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
                .orderBy(roomReservation.modifiedAt.desc())
                .fetch();
    }

    public List<RoomReservation> selectRoomNoReservation(int roomNo){
        return jpaQueryFactory
                .select(roomReservation)
                .from(roomReservation)
                .join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
                .where(roomReservation.meetingRoom.roomNo.eq(roomNo))
                .orderBy(roomReservation.modifiedAt.desc())
                .fetch();
    }

    public List<RoomReservation> selectDateTimeReservation(String startTime, String endTime) {
        return jpaQueryFactory
                .select(roomReservation)
                .from(roomReservation)
                .join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
                .where(roomReservation.startedAt.goe(LocalDateTime.parse(startTime))
                        .and(roomReservation.endedAt.loe(LocalDateTime.parse(endTime))))
                .orderBy(roomReservation.modifiedAt.desc())
                .fetch();
    }

    public List<RoomBookmarkDTO> selectTop3BookmarkMeetingRoom(long limit) {
        return jpaQueryFactory
                .select(new QRoomBookmarkDTO(
                        roomBookmark.id,
                        roomBookmark.employee.id.as("empId"),
                        roomBookmark.meetingRoom.id.as("roomId"),
                        roomBookmark.createdAt,
                        roomBookmark.modifiedAt,
                        roomBookmark.meetingRoom.id.count().as("count")
                ))
                .from(roomBookmark)
                .join(roomBookmark.meetingRoom, meetingRoom)
                .groupBy(meetingRoom.id)
                .orderBy(roomBookmark.meetingRoom.count().desc())
                .limit(limit)
                .fetch();
    }




}
