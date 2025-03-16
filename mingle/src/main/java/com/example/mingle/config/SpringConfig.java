package com.example.mingle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.mingle.repository.GuestRepository;
import com.example.mingle.restaurant.repository.RestaurantMenuRepository;
import com.example.mingle.restaurant.repository.RestaurantRepository;
import com.example.mingle.restaurant.service.RestaurantService;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final GuestRepository guestRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;

    @Bean
    public RestaurantService restaurantService() {
        return new RestaurantService(restaurantRepository,restaurantMenuRepository);
    }

}


