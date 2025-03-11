package com.example.mingle.reservation.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.reservation.domain.Reservation;
import com.example.mingle.reservation.repository.ReservationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 공통적으로 사용되는 guestId를 가져오는 메소드
    private Guest getGuestFromSession(HttpSession session) {
        Long guestId = (Long) session.getAttribute("guestId");
        if (guestId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return new Guest(); // guestId를 통해 Guest 객체를 생성하는 방식
    }
    private Guest getHostFromSession(HttpSession session) {
        Long hostId = (Long) session.getAttribute("hostId");
        if (hostId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return new Guest(); // hostId 통해 Guest 객체를 생성하는 방식
    }

    // 예약 현황 조회 (로그인한 사용자만 본인 예약을 조회)
    @GetMapping("/reservationStatus")
    public String getReservations(HttpSession session, Model model) {
        Long guest = (Long) session.getAttribute("guestId");
        Long host = (Long) session.getAttribute("hostId");
        System.out.println("게스트"+guest);
        System.out.println("호스트"+host);
        if (host == null) {
            List<Reservation> reservations = reservationRepository.findByGuest_Id(guest); // findByGuest_Id로 수정
            System.out.println("뺀 데이터"+reservations);
            model.addAttribute("reservations", reservations);
        }if(guest == null) {
            List<Reservation> reservations = reservationRepository.findByHost_Id(host); // findByHost_Id로 수정
            System.out.println("뺀 데이터"+reservations);
            model.addAttribute("reservations", reservations);
        }

        return "mypage/reservationStatus"; // 예약 목록을 보여주는 페이지
    }

    // 예약 추가 페이지 (form)
    @GetMapping("/reservations/add")
    public String addReservationForm() {
        return "/reservationStatus"; // 예약 추가 폼
    }

    // 예약 추가 처리
    @PostMapping("/reservations")
    public String addReservation(HttpSession session, @ModelAttribute Reservation reservation) {
        Guest guest = getGuestFromSession(session); // 세션에서 guest 가져오기
        reservation.setGuest(guest); // 예약에 guest 설정

        reservationRepository.save(reservation); // 예약 저장
        return "redirect:/reservationStatus"; // 예약 목록 페이지로 리다이렉트
    }

    // 예약 수정 페이지 (form)
    @GetMapping("/reservations/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. ID: " + id));

        model.addAttribute("reservation", reservation);
        return "/mypage/reservationEdit"; // 예약 수정 폼
    }

    // 예약 수정 처리
    @PostMapping("/reservations/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 기존 예약 데이터 업데이트
        existingReservation.setDate(reservation.getDate());
        existingReservation.setPeople(reservation.getPeople());
        existingReservation.setCancel(reservation.getCancel());
        existingReservation.setRestaurantId(reservation.getRestaurantId());
        existingReservation.setAccommodationRoomId(reservation.getAccommodationRoomId());

        reservationRepository.save(existingReservation); // 예약 저장

        return "redirect:/reservationStatus"; // 예약 목록 페이지로 리다이렉트
    }

    // 예약 삭제
    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, HttpSession session) {
        Guest guest = getGuestFromSession(session); // 세션에서 guest 가져오기

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 예약이 로그인한 사용자와 관련이 있는지 확인
        if (!reservation.getGuest().getId().equals(guest.getId())) {
            return "redirect:/reservationStatus"; // 권한이 없으면 예약 목록으로 리다이렉트
        }

        reservationRepository.delete(reservation); // 예약 삭제
        return "redirect:/reservationStatus"; // 예약 목록 페이지로 리다이렉트
    }
}
