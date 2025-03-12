package com.example.mingle.repository;

import com.example.mingle.domain.Couple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Long> {

    // ✅ guest1의 ID로 커플 조회
    @Query("SELECT c FROM Couple c WHERE c.guest1.id = :guest1Id")
    Couple findByGuest1Id(@Param("guest1Id") Long guest1Id);

    // ✅ guest2의 ID로 커플 조회
    @Query("SELECT c FROM Couple c WHERE c.guest2.id = :guest2Id")
    Couple findByGuest2Id(@Param("guest2Id") Long guest2Id);
}
