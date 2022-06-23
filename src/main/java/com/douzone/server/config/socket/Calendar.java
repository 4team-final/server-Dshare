package com.douzone.server.config.socket;

import com.douzone.server.config.utils.BaseAtTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "calendar")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends BaseAtTime {
	@Id
	private String uid;

}