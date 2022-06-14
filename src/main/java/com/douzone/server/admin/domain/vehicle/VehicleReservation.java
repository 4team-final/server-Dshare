package com.douzone.server.admin.domain.vehicle;

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
@Table(name = "vehicle_reservation")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReservation extends BaseAtTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehicleId;
    private String path;
    private String type;
    private String ImgSize;
}