package com.example.mingle.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class BottomController {


    @GetMapping("/bottom/careers")
    public String showcareers() {
        return "bottom/careers";
    }
}
