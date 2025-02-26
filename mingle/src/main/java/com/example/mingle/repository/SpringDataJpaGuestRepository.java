package com.example.mingle.repository;

import com.example.mingle.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaGuestRepository extends JpaRepository<Guest, Long>, GuestRepository {

    //JPQL select m from Member m where m.name = ?
    @Override
    Optional<Guest> findByName(String name);
}
