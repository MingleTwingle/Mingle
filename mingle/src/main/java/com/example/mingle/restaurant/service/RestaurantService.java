package com.example.mingle.restaurant.service;

import com.example.mingle.restaurant.domain.Restaurant;
import com.example.mingle.restaurant.domain.RestaurantMenu;
import com.example.mingle.restaurant.repository.RestaurantMenuRepository;
import com.example.mingle.restaurant.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMenuRepository restaurantMenuRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMenuRepository = restaurantMenuRepository;
    }

    public Long join(Restaurant restaurant) {
        try {
            validateDuplicationMember(restaurant);
            restaurantRepository.save(restaurant);
            return restaurant.getId();
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


    public List<Restaurant> searchRestaurant(String location, LocalTime openTime, LocalTime endTime) {
        List<Restaurant> filteredRestaurants = restaurantRepository.findAll().stream()
                .filter(a -> a.getRestaurantLocation().equals(location))
                .filter(a -> openTime == null || a.getRestaurantOpenTime() == null || !a.getRestaurantOpenTime().isBefore(openTime))
                .filter(a-> endTime == null || a.getRestaurantEndTime() == null || !a.getRestaurantEndTime().isAfter(endTime))
                .collect(Collectors.toList());

        filteredRestaurants.forEach(System.out::println);
        return filteredRestaurants;
    }
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    // 🔹 특정 식당의 메뉴 리스트 조회
    public List<RestaurantMenu> getMenusByRestaurantId(Long restaurantId) {
        return restaurantMenuRepository.findByRestaurantId(restaurantId);
    }
}
