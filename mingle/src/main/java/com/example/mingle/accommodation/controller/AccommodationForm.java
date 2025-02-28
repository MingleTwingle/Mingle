package com.example.mingle.accommodation.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccommodationForm {

    private String name;
    private String location;
    private boolean parkingAvailable;
    private boolean morningAvailable;
    private int checkinTime;
}
