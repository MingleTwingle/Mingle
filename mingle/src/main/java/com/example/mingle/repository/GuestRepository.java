package com.example.mingle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mingle.domain.Guest;


@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>  {
    Optional<Guest> findByIdid(String idid);
    Optional<Guest> findByName(String name);
    Optional<Guest> findByCoupleCode(String coupleCode); // 커플 코드 찾기
}