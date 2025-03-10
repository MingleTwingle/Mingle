package com.example.mingle.accommodation.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class AccommodationFilterForm {

    private String location;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
}
