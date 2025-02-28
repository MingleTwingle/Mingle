package com.example.mingle.accommodation.service;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.repository.AccommodationRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        List<Accommodation> accommodation = accommodationRepository.findAll().stream().filter(a -> a.getLocation().equals(location)).collect(Collectors.toList());
        accommodation.forEach(System.out::println);

//        System.out.println("FINDALL: " + accommodationRepository.findAll().stream());
//        System.out.println(accommodationRepository.findAll().stream().filter(a -> a.getLocation().equalsIgnoreCase(location)).collect(Collectors.toList()));
        return accommodationRepository.findAll().stream()
                .filter(a -> a.getLocation().equals(location) && a.getCheckInTime().isEqual(checkInTime) && a.getCheckOutTime().isEqual(checkOutTime))
                .collect(Collectors.toList());
    }

    public List<Accommodation> findAccommodation() {
        return accommodationRepository.findAll();
    }
}

