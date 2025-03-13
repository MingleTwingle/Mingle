package com.example.mingle.reservation.service;

import com.example.mingle.reservation.domain.Reservation;
import com.example.mingle.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;  // static 제거

    // 예약 조회 (사용자의 예약 목록 반환)
    public List<Reservation> getReservationsByGuest(Long guestId) {
        return reservationRepository.findByGuest_Id(guestId);
    }

    public List<Reservation> getReservationsByHost(Long hostId) {
        return reservationRepository.findByHost_Id(hostId);
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
            reservationRepository.save(reservation);
        }
    }

    // 예약 ID로 restaurant name 조회
    public String getRestaurantNameByReservationId(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return reservation.getRestaurant().getRestaurantName();  // restaurant에서 restaurantName 반환
    }

    // 예약 취소
    public void cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
