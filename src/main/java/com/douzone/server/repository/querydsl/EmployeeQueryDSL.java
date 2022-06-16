package com.douzone.server.repository.querydsl;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.MeetingRoom;
import com.douzone.server.entity.QRoomReservation;
import com.douzone.server.entity.RoomReservation;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.douzone.server.entity.QEmployee.employee;
import static com.douzone.server.entity.QMeetingRoom.meetingRoom;
import static com.douzone.server.entity.QRoomReservation.roomReservation;
import static com.douzone.server.entity.QPosition.position;
import static com.douzone.server.entity.QTeam.team;


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
                .join(roomReservation.meetingRoom, meetingRoom).fetchJoin()
               .where(roomReservation.startedAt.gt(LocalDateTime.now()))
                .orderBy(roomReservation.modifiedAt.desc())
                .limit(top)
                .fetch();
    }
    /**
     *  select emp.*, tName, p.name, dName from (select t.id, t.name tName, d.name dName from team t inner join department d on t.deptId = d.id) td
     *           inner join employee emp
     *                    on emp.teamId = td.id
     *              inner join position p
     *                     on emp.positionId = p.id
     *              where emp.empNo = ?;
     */
    public List<Employee> findMyProfile(long id) {
        return jpaQueryFactory
                .select(employee).from(employee)
                .join(employee.position, position).fetchJoin()
                .join(employee.team, team).fetchJoin()
                .where(employee.id.eq(id))
                .fetch();
    }




}
