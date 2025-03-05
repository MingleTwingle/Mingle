package com.example.mingle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BottomController {


    @GetMapping("/bottom/careers")
    public String careers() {
        return "bottom/careers";  // templates/bottom/careers.html로 이동
    }

    @GetMapping("/bottom/blog")
    public String blog() {
        return "bottom/blog";  // templates/bottom/blog.html로 이동
    }

    @GetMapping("/bottom/press")
    public String press() {
        return "bottom/press";  // templates/bottom/press.html로 이동
    }

    @GetMapping("/bottom/packages")
    public String packages() {
        return "bottom/packages";  // templates/bottom/packages.html로 이동
    }

    @GetMapping("/bottom/gift-cards")
    public String giftCards() {
        return "bottom/gift-cards";  // templates/bottom/gift-cards.html로 이동
    }

    @GetMapping("/bottom/help")
    public String help() {
        return "bottom/help";  // templates/bottom/help.html로 이동
    }

    @GetMapping("/bottom/faq")
    public String faq() {
        return "bottom/faq";  // templates/bottom/faq.html로 이동
    }

    @GetMapping("/bottom/partners")
    public String partners() {
        return "bottom/partners";  // templates/bottom/partners.html로 이동
    }

    @GetMapping("/bottom/terms")
    public String terms() {
        return "bottom/terms";  // templates/bottom/terms.html로 이동
    }

    @GetMapping("/bottom/privacy")
    public String privacy() {
        return "bottom/privacy";  // templates/bottom/privacy.html로 이동
    }

    @GetMapping("/bottom/cookies")
    public String cookies() {
        return "bottom/cookies";  // templates/bottom/cookies.html로 이동
    }
}
