package com.example.mingle.accommodation.repository;

import com.example.mingle.accommodation.domain.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository extends JpaRepository<Accommodation, String> {
    Optional<Accommodation> findByName(String name);

    List<Accommodation> findByLocationContaining(String location);

}
