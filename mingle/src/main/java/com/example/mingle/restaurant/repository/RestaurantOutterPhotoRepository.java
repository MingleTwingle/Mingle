package com.example.mingle.restaurant.repository;

import com.example.mingle.restaurant.domain.RestaurantOutterPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantOutterPhotoRepository extends JpaRepository<RestaurantOutterPhoto, Long> {
    List<RestaurantOutterPhoto> findByRestaurantId(Long restaurantId);
}
