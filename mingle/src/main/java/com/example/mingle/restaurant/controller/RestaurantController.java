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
import org.springframework.web.bind.annotation.ModelAttribute;
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
        return "redirect:/";
    }

    @GetMapping("/restaurants/list")
    public String list(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurant();
        model.addAttribute("restaurants", restaurants);
        return "restaurant/restaurantList";
    }

    @GetMapping("/restaurants/filter")
    public String filterList(Model model) { // 메서드 이름 변경!
        List<Restaurant> restaurants = restaurantService.findRestaurant();
        model.addAttribute("restaurants", restaurants);
        return "restaurant/restaurantFilter";
    }
    @PostMapping("/restaurants/filter")
    public String customFilter(@Validated RestaurantFilterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "restaurant/restaurantFilter";
        }

        System.out.println("Location: " + form.getRestaurantLocation());
        System.out.println("Open Time: " + form.getRestaurantOpenTime());
        System.out.println("End Time: " + form.getRestaurantEndTime());

        // 필터링된 레스토랑 목록 검색
        List<Restaurant> filteredRestaurants = restaurantService.filterRestaurants(
                form.getRestaurantName(),
                form.getRestaurantLocation(),
                form.getRestaurantParking() != null ? form.getRestaurantParking() : false,
                form.getRestaurantOpenTime(),
                form.getRestaurantEndTime()
        );

        // 모델에 필터링된 레스토랑 추가
        model.addAttribute("restaurants", filteredRestaurants);
        return "restaurant/restaurantFilterList"; // 필터링된 레스토랑 리스트 페이지로 이동
    }

    @GetMapping("/restaurants/filterList")
    public String showFilterList(Model model) {
        return "restaurant/restaurantFilterList";
    }
}
