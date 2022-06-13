package com.douzone.server.employee.domain.employee;

import com.douzone.server.config.utils.BaseAtTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "employee")
@Getter
@Builder
@NoArgsConstructor
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "positionId")
	private Position position;
}
