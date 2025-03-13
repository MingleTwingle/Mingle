package com.example.mingle.accommodation.service;

import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.accommodation.repository.AccommodationRoomRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
public class AccommodationRoomService {

    private final AccommodationRoomRepository accommodationRoomRepository;

    public AccommodationRoomService(AccommodationRoomRepository accommodationRoomRepository) {
        this.accommodationRoomRepository = accommodationRoomRepository;
    }

    public List<AccommodationRoom> findByAccommodationId(Long accommodationId) {
        return accommodationRoomRepository.findByAccommodationId(accommodationId);
    }

    public List<AccommodationRoom> getRoomsByAccommodationId(Long accommodationId) {
        return accommodationRoomRepository.findByAccommodationId(accommodationId);
    }
}
