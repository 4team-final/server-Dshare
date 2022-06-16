package com.douzone.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "token")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

	@Id
	private String empNo;
	private String refreshToken;
}
