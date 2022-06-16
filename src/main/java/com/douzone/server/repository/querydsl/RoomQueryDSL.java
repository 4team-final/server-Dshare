package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QPosition.position;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QTeam.team;


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

}
