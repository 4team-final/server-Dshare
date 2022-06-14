package com.douzone.server.dto.employee;

import com.douzone.server.entity.Employee;
import com.douzone.server.entity.Position;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EmpTestDTO {
    private Long id;
    private String empNo;
    private String name;
    private String email;
    private String tel;
    private LocalDateTime birthday;
    private String profileImg;
    private String role;
    private Position position;

    @Builder
    public EmpTestDTO(Long id, String empNo, String name, String email, String tel, LocalDateTime birthday, String profileImg, String role, Position position) {
        this.id = id;
        this.empNo = empNo;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.birthday = birthday;
        this.profileImg = profileImg;
        this.role = role;
        this.position = position;
    }


    public EmpTestDTO of(Employee employee) {
        return EmpTestDTO.builder().id(employee.getId()).empNo(employee.getEmpNo()).name(employee.getName()).email(employee.getEmail()).tel(employee.getTel()).birthday(employee.getBirthday()).profileImg(employee.getProfileImg()).position(employee.getPosition()).build();
    }
}
