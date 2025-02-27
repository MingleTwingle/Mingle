package com.example.mingle.restaurant.repository;

import com.example.mingle.restaurant.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long restaurantId);
    Optional<Restaurant> findByRestaurantName(String restaurantName);
    List<Restaurant> findAll();
}
