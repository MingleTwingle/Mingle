package com.example.mingle.accommodation.repository;

import com.example.mingle.accommodation.domain.AccommodationRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRoomRepository extends JpaRepository<AccommodationRoom, Long> {
    List<AccommodationRoom> findByAccommodationId(Long accommodationId);
}
