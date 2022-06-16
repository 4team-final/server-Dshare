package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.RoomBookmark;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.core.Tuple;
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

    public List<RoomReservation> selectDateTimeReservation(String date) {
        return jpaQueryFactory
                .select(roomReservation)
                .from(roomReservation)
                .join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
                .where(roomReservation.startedAt.eq(LocalDateTime.parse(date)))
                .orderBy(roomReservation.modifiedAt.desc())
                .fetch();
    }

    public List<RoomBookmark> selectTop3BookmarkMeetingRoom() {
        return jpaQueryFactory
                .select(roomBookmark)
                .from(roomBookmark)
                .join(roomBookmark.meetingRoom, meetingRoom).fetchJoin()
                .groupBy(meetingRoom.id)
                .orderBy(roomBookmark.meetingRoom.id.count().desc())
                .limit(3)
                .fetch();
    }


}
