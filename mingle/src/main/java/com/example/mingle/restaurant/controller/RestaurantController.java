package com.example.mingle.restaurant.controller;

import com.example.mingle.accommodation.controller.AccommodationFilterForm;
import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.restaurant.domain.Restaurant;
import com.example.mingle.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants/new")
    public String createForm() {
        return "restaurant/register";
    }

    @PostMapping("/restaurants/new")
    public String create(@Validated RestaurantForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "restaurant/register";
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(form.getName());
        restaurant.setRestaurantLocation(form.getLocation());
        restaurant.setRestaurantParking(form.isParking());
        restaurant.setRestaurantOpenTime(form.getOpenTime());
        restaurant.setRestaurantEndTime(form.getEndTime());
        restaurantService.join(restaurant);
        return "redirect:/restaurants/list";
    }

    // 관리자 식당 목록 조회
    @GetMapping("/restaurants/list")
    public String list(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurant();
        model.addAttribute("restaurants", restaurants);
        return "restaurant/restaurantList";
    }

    @GetMapping("/restaurants/filter")
    public String filterList(Model model) {
        if (!model.containsAttribute("restaurantFilterForm")) {
            model.addAttribute("restaurantFilterForm", new RestaurantFilterForm());
        }
        List<Restaurant> restaurants = restaurantService.findRestaurant();
        model.addAttribute("restaurants", restaurants);
        return "restaurant/restaurantFilter";
    }

    @PostMapping("/restaurants/filter")
    public String customFilter(@Validated RestaurantFilterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "restaurant/restaurantFilter";
        }

        List<Restaurant> filteredRestaurants;
        if (isFilterEmpty(form)) {
            filteredRestaurants = restaurantService.findRestaurant(); // 모든 숙소 반환
            System.out.println("입력값 없음 -> 전체 식당 반환: " + filteredRestaurants.size());
        } else {
            filteredRestaurants = restaurantService.searchRestaurant(form.getRestaurantLocation(), form.getRestaurantOpenTime(), form.getRestaurantEndTime());
            System.out.println("필터링된 식당 개수: " + filteredRestaurants.size());
        }

        model.addAttribute("restaurants", filteredRestaurants);
        return "restaurant/restaurantFilterList";
    }

    private boolean isFilterEmpty(RestaurantFilterForm form) {
        return (form.getRestaurantLocation() == null || form.getRestaurantLocation().isBlank()) &&
                (form.getRestaurantOpenTime() == null) &&
                (form.getRestaurantEndTime() == null);
    }

    @GetMapping("/restaurants/filterList")
    public String showFilterList(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurant();
        model.addAttribute("restaurants", restaurants);
        return "restaurant/restaurantFilterList";
    }
}
