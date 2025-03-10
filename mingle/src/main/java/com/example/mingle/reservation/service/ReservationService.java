package com.example.mingle.reservation.service;

import com.example.mingle.reservation.domain.Reservation;

import com.example.mingle.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // 예약 조회 (사용자의 예약 목록 반환)
    public List<Reservation> getReservationsByGuest(Long guestId) {
        return reservationRepository.findByGuestId(guestId);
    }

    // 특정 예약 조회
    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    // 예약 수정
    public void updateReservation(Long reservationId, String newDate, String newTime) {
        Reservation reservation = getReservationById(reservationId);
        if (reservation != null) {
            reservation.setDate(newDate);
            reservation.setTime(newTime);
            reservationRepository.save(reservation);
        }
    }

    // 예약 취소
    public void cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}

