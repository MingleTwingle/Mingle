package com.example.mingle.restaurant.service;

import com.example.mingle.restaurant.domain.Restaurant;
import com.example.mingle.restaurant.repository.RestaurantRepository;

import java.time.LocalTime;
import java.util.List;

public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Long join(Restaurant restaurant) {
        try {
            validateDuplicationMember(restaurant);
            restaurantRepository.save(restaurant);
            return restaurant.getRestaurant_id();
        } catch (Exception e) {
            System.out.println("Error saving restaurant: " + e.getMessage());
            throw e;
        }

    }

    private void validateDuplicationMember(Restaurant restaurant) {
        restaurantRepository.findByRestaurantName(restaurant.getRestaurantName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 식당입니다.");
                });
    }

    public List<Restaurant> findRestaurant() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> filterRestaurants(String restaurantName, String restaurantLocation, Boolean restaurantParking, LocalTime restaurantOpenTime, LocalTime restaurantEndTime) {
        return restaurantRepository.findByFilters(
                restaurantName,
                restaurantLocation,
                restaurantParking,
                restaurantOpenTime,
                restaurantEndTime
        );
    }
}
