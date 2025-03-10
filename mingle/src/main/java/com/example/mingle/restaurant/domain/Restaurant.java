package com.example.mingle.restaurant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_restaurant_info")
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;
    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;
    @Column(name = "restaurant_location", nullable = false)
    private String restaurantLocation;
    @Column(name = "restaurant_parking", nullable = false)
    private boolean restaurantParking;
    @Column(name = "restaurant_open_time", nullable = false)
    private LocalTime restaurantOpenTime;
    @Column(name = "restaurant_end_time", nullable = false)
    private LocalTime restaurantEndTime;


    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantOutterPhoto> outterPhotos;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantMenu> menus;


}
