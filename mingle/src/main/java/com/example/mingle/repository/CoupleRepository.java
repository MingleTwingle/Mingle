package com.example.mingle.repository;

import com.example.mingle.domain.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoupleRepository extends JpaRepository<Couple, Long> {

    Couple findByGuest1Id(Long guest1Id);

    Couple findByGuest2Id(Long guest2Id);

    @Query("SELECT c FROM Couple c " +
            "WHERE c.guest1.id = :guestId OR c.guest2.id = :guestId")
    Couple findByGuestId(@Param("guestId") Long guestId);
}
