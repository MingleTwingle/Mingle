package com.example.mingle.accommodation.service;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.repository.AccommodationRepository;
import com.example.mingle.restaurant.domain.Restaurant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;

    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    public Integer join(Accommodation accommodation) {
        try {
            validateDuplicationMember(accommodation);
            accommodationRepository.save(accommodation);
            return accommodation.getId();
        } catch (Exception e) {
            System.out.println("Error saving accommodation: " + e.getMessage());
            throw e;
        }

    }

    private void validateDuplicationMember(Accommodation accommodation) {
        accommodationRepository.findByName(accommodation.getName())
                .ifPresent( m -> {
                    throw new IllegalStateException("이미 존재하는 숙소입니다.");
                });
    }
    //지역으로 숙소 찾기
    public List<Accommodation> searchAccommodation(String location) {
        return accommodationRepository.findByLocationContaining(location);
    }

    public List<Accommodation> findAccommodation() {
        return accommodationRepository.findAll();
    }
}

