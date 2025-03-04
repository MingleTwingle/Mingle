package com.example.mingle.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/login")
    public String login(@RequestParam("idid") String idid, @RequestParam("password") String password, Model model) {
        boolean isValidGuest = guestService.validateLogin(idid, password);
        if (isValidGuest) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid ID or password. Please try again.");
            return "/login";
        }
    }


    @GetMapping("/about")
    public String showabout() {
        return "/about";
    }
    @GetMapping("/contact")
    public String showcontact() {
        return "/contact";
    }

    @GetMapping("/host/register")
    public String showhostRegisterForm() {
        return "host/register";
    }

    @GetMapping("/guestOrHost")
    public String showguestOrHost() {
        return "guestOrHost";
    }

    @GetMapping("/guests/register")
    public String createRegisterForm() {
        return "guest/register";
    }

    @PostMapping("/guests/register")
    public String create(@Validated GuestForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "guest/register";
        }
        Guest guest = new Guest();
        guest.setName(form.getGuest_name());
        guest.setIdid(form.getGuest_idid());
        guest.setNickname(form.getGuest_nickname());
        guest.setPassword(form.getGuest_password());
        guest.setEmail(form.getGuest_email());
        guest.setGender(form.getGuest_gender());
        System.out.println("GUEST GENDER: " + guest.getGender());
        guest.setPhone(form.getGuest_phone_number());
        guestService.join(guest);
        return "redirect:/login";
    }

    @GetMapping("/guests/list")
    public String list(Model model) {
        List<Guest> guests = guestService.findUser();
        model.addAttribute("guests", guests);
        return "guest/guestList";
    }
}
