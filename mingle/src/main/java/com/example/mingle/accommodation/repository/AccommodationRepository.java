package com.example.mingle.accommodation.repository;


import com.example.mingle.accommodation.domain.Accommodation;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository {
    Accommodation save(Accommodation accommodation);
    Optional<Accommodation> findById(Long accommodationId);
    Optional<Accommodation> findByName(String name);
    List<Accommodation> findAll();
    List<Accommodation> findByLocationContaining(String location);
}
