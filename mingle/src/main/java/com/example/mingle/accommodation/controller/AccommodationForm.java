package com.example.mingle.accommodation.controller;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationForm {

    private String name;
    private String location;
    private boolean parkingAvailable;
    private boolean morningAvailable;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
}
