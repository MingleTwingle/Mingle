package com.example.mingle.controller;

import com.example.mingle.domain.Guest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoupleForm {
    private Long id;
    private Guest guest1;
    private Guest guest2;
}
