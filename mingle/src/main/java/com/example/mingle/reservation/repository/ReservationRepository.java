package com.example.mingle.reservation.repository;

import com.example.mingle.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByGuestId(Long guestId);

    List<Reservation> findByGuest_Id(Long guestId);


}
