package com.douzone.server.admin.dto.employee;


import com.douzone.server.employee.domain.employee.Employee;
import com.douzone.server.employee.domain.employee.Position;
import com.douzone.server.employee.domain.employee.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SignupReqDTO {

    @NotNull(message = "팀 Id가 null 일 수 없습니다.")
    private int teamId;
    @NotNull(message = "직책 Id가 null 일 수 없습니다.")
    private int positionId;
    @NotNull(message = "사번은 null 일 수 가 없습니다.")
    private String empNo;
    private String password;
    private String name;
    private String email;
    private String tel;
    private LocalDateTime birthday;

    @Builder
    public SignupReqDTO(int teamId, int positionId, String empNo, String password, String name, String email, String tel, LocalDateTime birthday) {
        this.teamId = teamId;
        this.positionId = positionId;
        this.empNo = empNo;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.birthday = birthday;
    }

    public Employee of(Team team, Position position) {
        return Employee.builder()
                .empNo(empNo)
                .password(password)
                .name(name)
                .email(email)
                .tel(tel)
                .birthday(birthday)
                .team(team)
                .position(position)
                .build();
    }
}
