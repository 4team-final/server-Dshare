package com.douzone.server.entity;

import com.douzone.server.config.security.handler.DecodeEncodeHandler;
import com.douzone.server.config.utils.BaseAtTime;
import com.douzone.server.dto.employee.SignModReqDTO;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "employee")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Employee extends BaseAtTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String empNo;
    private String password;
    private String name;
    private String email;
    private String tel;
    private LocalDateTime birthday;
    private String profileImg;
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "positionId")
    private Position position;

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void update(SignModReqDTO signModReqDTO) {
        this.name = signModReqDTO.getName();
        this.email = signModReqDTO.getEmail();
        this.birthday = signModReqDTO.getBirthday();
        this.tel = signModReqDTO.getTel();
    }
    public void update(String newPassword) {
        this.password = newPassword;

    }

}

