package com.example.mingle.accommodation.controller;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationFilterForm {

    private String location;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
}
