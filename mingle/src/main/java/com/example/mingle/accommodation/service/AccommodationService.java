package com.example.mingle.accommodation.service;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.repository.AccommodationRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Accommodation> searchAccommodation(String location, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        List<Accommodation> filteredAccommodations = accommodationRepository.findAll().stream()
                .filter(a -> a.getLocation().equals(location))
                .filter(a -> checkInTime == null || a.getCheckInTime() == null || !a.getCheckInTime().isBefore(checkInTime))
                .filter(a -> checkOutTime == null || a.getCheckOutTime() == null || !a.getCheckOutTime().isAfter(checkOutTime))
                .collect(Collectors.toList());

        // 필터링된 숙소 목록을 콘솔에 출력
        filteredAccommodations.forEach(System.out::println);

        return filteredAccommodations;
    }


    public List<Accommodation> findAccommodation() {
        return accommodationRepository.findAll();
    }
}

