package com.example.mingle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mingle.domain.Guest;



@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>  {
    List<Guest> findByName(String name);
    Optional<Guest> findByIdid(String idid);
    Optional<Guest> findByCoupleCode(String coupleCode); // 커플 코드 찾기
}