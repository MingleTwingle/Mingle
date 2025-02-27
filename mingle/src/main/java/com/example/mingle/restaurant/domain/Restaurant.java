package com.example.mingle.restaurant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_restaurant_info")
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurant_id;
    @Column(name = "restaurant_Name", nullable = false)
    private String restaurantName;
    @Column(name = "restaurant_Location", nullable = false)
    private String restaurantLocation;
    @Column(name = "restaurant_Parking", nullable = false)
    private boolean restaurantParking;
    @Column(name = "restaurant_open_time", nullable = false)
    private LocalTime restaurantOpenTime;
    @Column(name = "restaurant_end_time", nullable = false)
    private LocalTime restaurantEndTime;
}
