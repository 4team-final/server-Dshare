package com.douzone.server.entity;

import com.douzone.server.config.utils.BaseAtTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "vehicle")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle extends BaseAtTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String number;
	private String model;
	private String color;
	private int capacity;
}
