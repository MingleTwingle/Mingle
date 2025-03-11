package com.example.mingle.restaurant.repository;

import com.example.mingle.restaurant.domain.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
    List<RestaurantMenu> findByRestaurantId(Long restaurantId);
}

