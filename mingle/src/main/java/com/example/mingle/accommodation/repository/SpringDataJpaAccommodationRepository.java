package com.example.mingle.accommodation.repository;

import com.example.mingle.accommodation.domain.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaAccommodationRepository extends JpaRepository<Accommodation, Long>, AccommodationRepository {

    @Override
    Optional<Accommodation> findByName(String name);
}
