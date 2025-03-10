package com.example.mingle.accommodation.repository;

import com.example.mingle.accommodation.domain.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    // 특정 이름으로 숙소 찾기 (Optional 사용)
    Optional<Accommodation> findByName(String name);

    // 특정 지역이 포함된 숙소 검색 (LIKE 검색)
    List<Accommodation> findByLocationContaining(String location);
}
