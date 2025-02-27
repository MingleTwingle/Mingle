package com.example.mingle.restaurant.repository;

import com.example.mingle.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaRestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantRepository{

    @Override
    Optional<Restaurant> findByRestaurantName(String restaurantName);
}
