package com.douzone.server.employee.domain.employee;

import com.douzone.server.config.utils.BaseAtTime;

import java.util.Date;

public class Employee extends BaseAtTime {

	private Long id;
	private Long empNo;
	private String password;
	private String name;
	private String email;
	private String tel;
	private Date birthday;
	private String profileImg;
	private String createBy;
	private String modifiedBy;

	private Team team;
	private Position position;
	private Role role;
}
