package com.douzone.server.employee.domain.employee;

import com.douzone.server.config.utils.BaseAtTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@DynamicUpdate
@Entity(name = "Employee")
@Getter
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
	private Date birthday;
	private String profileImg;
	private String role;
	private String createBy;
	private String modifiedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teamId")
	private Team team;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "positionId")
	private Position position;
}
