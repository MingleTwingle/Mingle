package com.example.mingle.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GuestController {
    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "/login";
    }

    @GetMapping("/about")
    public String showabout() {
        return "/about";
    }
    @GetMapping("/contact")
    public String showcontact() {
        return "/contact";
    }

    @GetMapping("/guests/register")
    public String showguestsRegisterForm() {
        return "guest/register";
    }

    @GetMapping("/host/register")
    public String showhostRegisterForm() {
        return "host/register";
    }

    @GetMapping("/guestOrHost")
    public String showguestOrHost() {
        return "guestOrHost";
    }


    @PostMapping("/guests/new")
    public String create(GuestForm form) {
        Guest guest = new Guest();
        guest.setName(form.getName());
        guestService.join(guest);
        return "redirect:/";
    }

    @GetMapping("/guests/list")
    public String list(Model model) {
        List<Guest> guests = guestService.findUser();
        model.addAttribute("guests", guests);
        return "guest/guestList";
    }
}
