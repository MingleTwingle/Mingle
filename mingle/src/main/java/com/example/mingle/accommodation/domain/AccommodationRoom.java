package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_accommodationroom_info")
public class AccommodationRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodationRoom_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")  // 숙소와 연결된 외래 키
    private Accommodation accommodation;

    @Column(name = "accommodationRoom_cost")
    private int cost;

    @Column(name = "accommodationRoom_min")
    private int minCapacity;

    @Column(name = "accommodationRoom_max")
    private int maxCapacity;

    @Column(name = "accommodationRoom_name")
    private String name;
}
