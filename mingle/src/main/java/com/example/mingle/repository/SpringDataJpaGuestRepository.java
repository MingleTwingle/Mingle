package com.example.mingle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mingle.domain.Guest;

public interface SpringDataJpaGuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByName(String name);
}
