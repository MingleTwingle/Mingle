package com.example.mingle.accommodation.controller;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.service.AccommodationService;
import com.example.mingle.restaurant.controller.RestaurantForm;
import com.example.mingle.restaurant.domain.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AccommodationController {

    private final AccommodationService accommodationService;

    @Autowired
    public AccommodationController(AccommodationService accommodationService) {
        this.accommodationService = accommodationService;
    }


    //  (register.html)
    @GetMapping("/accommodation/new")
    public String createForm() {
        return "accommodation/register";

    }

    @PostMapping("/accommodation/new")
    public String create(@Validated AccommodationForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "accommodation/register";
        }
        Accommodation accommodation = new Accommodation();
        accommodation.setName(form.getName());
        accommodation.setLocation(form.getLocation());
        accommodation.setParkingAvailable(form.isParkingAvailable());
        accommodation.setMorningAvailable(form.isMorningAvailable());
        accommodation.setCheckInTime(form.getCheckInTime());
        accommodation.setCheckOutTime(form.getCheckOutTime());
        accommodationService.join(accommodation);
        return "redirect:/";
    }

    @GetMapping("/accommodation/list")
    public String listAll(Model model) {
        List<Accommodation> accommodations = accommodationService.findAccommodation();
        model.addAttribute("accommodation", accommodations);
        return "accommodation/accommodationList";
    }

    @GetMapping("/accommodation/filter")
    public String filterList(Model model) {
        List<Accommodation> accommodations = accommodationService.findAccommodation();
        model.addAttribute("accommodation", accommodations);
        return "accommodation/accommodationFilter";
    }

    @PostMapping("/accommodation/filter")
    public String customFilter(@Validated AccommodationFilterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "accommodation/accommodationFilter";
        }
        List<Accommodation> filteredAccommodations = accommodationService.searchAccommodation(form.getLocation(), form.getCheckInTime(), form.getCheckOutTime());
        model.addAttribute("accommodations", filteredAccommodations);
        return "accommodation/accommodationFilterList";
    }

    @GetMapping("/accommodation/filterList")
    public String showFilterList(Model model) {
        List<Accommodation> accommodations = accommodationService.findAccommodation();
        return "accommodation/accommodationFilterList";
    }
}
