package com.example.mingle;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
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

    @Autowired
    public SpringConfig(GuestRepository guestRepository, RestaurantRepository restaurantRepository) {
        this.guestRepository = guestRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Bean
    public GuestService guestService() {
        return new GuestService(guestRepository);
    }

    @Bean
    public RestaurantService restaurantService() {
        return new RestaurantService(restaurantRepository);
    }

}


