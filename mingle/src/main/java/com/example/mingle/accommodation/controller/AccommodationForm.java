package com.example.mingle.accommodation.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
