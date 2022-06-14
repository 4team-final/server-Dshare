package com.douzone.server.entity;

import com.douzone.server.config.utils.BaseAtTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "position")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Lazy
public class Position extends BaseAtTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

