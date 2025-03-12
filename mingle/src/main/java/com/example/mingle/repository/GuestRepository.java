package com.example.mingle.repository;

import com.example.mingle.domain.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>  {
    Guest save(Guest guest);
    Optional<Guest> findByIdid(String idid);
    Optional<Guest> findByName(String name);
    List<Guest> findAll();
    Optional<Guest> findByCoupleCode(String coupleCode); // 커플 코드 찾기
}