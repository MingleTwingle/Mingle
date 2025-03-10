package com.example.mingle.restaurant.repository;

import com.example.mingle.restaurant.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(Long restaurantId);
    Optional<Restaurant> findByRestaurantName(String restaurantName);
    List<Restaurant> findAll();

    @Query("SELECT r FROM Restaurant r " +
            "WHERE (:restaurantName IS NULL OR r.restaurantName LIKE %:restaurantName%) " +
            "AND (:restaurantLocation IS NULL OR r.restaurantLocation LIKE %:restaurantLocation%) " +
            "AND (:restaurantParking IS NULL OR r.restaurantParking = :restaurantParking OR r.restaurantParking IS TRUE) " +
            "AND (:restaurantOpenTime IS NULL OR r.restaurantOpenTime <= :restaurantOpenTime) " +
            "AND (:restaurantEndTime IS NULL OR r.restaurantEndTime >= :restaurantEndTime)")
    List<Restaurant> findByFilters(
            @Param("restaurantName") String restaurantName,
            @Param("restaurantLocation") String restaurantLocation,
            @Param("restaurantParking") Boolean restaurantParking,
            @Param("restaurantOpenTime") LocalTime restaurantOpenTime,
            @Param("restaurantEndTime") LocalTime restaurantEndTime);
}
