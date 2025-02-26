package com.example.mingle;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {

    private final GuestRepository guestRepository;

    @Autowired
    public SpringConfig(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Bean
    public GuestService guestService() {
        return new GuestService(guestRepository);
    }

}


