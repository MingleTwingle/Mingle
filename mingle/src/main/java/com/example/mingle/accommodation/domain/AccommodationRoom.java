package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_accommodation_room_info")
public class AccommodationRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_room_id")  // 기본 키 컬럼명 'accommodation_room_Id'
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")  // 숙소와 연결된 외래 키
    private Accommodation accommodation;

    @Column(name = "accommodation_room_cost")
    private int cost;

    @Column(name = "accommodation_room_name")
    private String name;
}
