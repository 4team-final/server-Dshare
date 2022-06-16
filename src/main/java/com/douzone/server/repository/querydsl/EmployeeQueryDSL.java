package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomReservation.roomReservation;


@Repository
@RequiredArgsConstructor
public class EmployeeQueryDSL {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Employee> findEmployeeList(long positionId) {
        return jpaQueryFactory.select(employee)
                .from(employee).leftJoin(employee.position).fetchJoin()
                .where(employee.position.id.eq(positionId)).fetch();
    }

    public List<RoomReservation> reservedLatestRoomTop3(int top) {
        return jpaQueryFactory
                .select(roomReservation).from(roomReservation)
                .join(roomReservation.meetingRoom, meetingRoom)
               .where(roomReservation.startedAt.gt(LocalDateTime.now()))
                .orderBy(roomReservation.modifiedAt.desc())
                .limit(top)
                .fetch();
    }
}
