package com.example.mingle.repository;

import com.example.mingle.domain.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestRepository {
    Guest save(Guest guest);
    Optional<Guest> findById(Long id);
    Optional<Guest> findByName(String name);
    List<Guest> findAll();
}