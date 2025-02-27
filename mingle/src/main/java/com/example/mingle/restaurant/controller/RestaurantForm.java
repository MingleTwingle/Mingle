package com.example.mingle.restaurant.controller;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class RestaurantForm {
    private String name;
    private String location;
    private boolean parking;
    private LocalTime openTime;
    private LocalTime endTime;
}
