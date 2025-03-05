package com.example.mingle.restaurant.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

@Getter
@Setter
public class RestaurantFilterForm {
    private String restaurantLocation;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime restaurantOpenTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime restaurantEndTime;
}
