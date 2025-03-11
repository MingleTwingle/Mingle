package com.example.mingle.config;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.restaurant.repository.RestaurantMenuRepository;
import com.example.mingle.restaurant.repository.RestaurantRepository;
import com.example.mingle.restaurant.service.RestaurantService;
import com.example.mingle.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {

    private final GuestRepository guestRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;

    @Autowired
    public SpringConfig(GuestRepository guestRepository, RestaurantRepository restaurantRepository, RestaurantMenuRepository restaurantMenuRepository) {
        this.guestRepository = guestRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantMenuRepository = restaurantMenuRepository;
    }



    @Bean
    public RestaurantService restaurantService() {
        return new RestaurantService(restaurantRepository,restaurantMenuRepository);
    }

}


