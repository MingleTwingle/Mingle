package com.example.mingle.reservation.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.reservation.domain.Reservation;
import com.example.mingle.reservation.repository.ReservationRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;

    @Autowired
    private GuestRepository guestRepository; // GuestRepository 주입

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 공통적으로 사용되는 guestId를 가져오는 메소드
    private Guest getGuestFromSession(HttpSession session) {
        Long guestId = (Long) session.getAttribute("guestId");
        System.out.println("세션 사용자 아이디: " + (Long) session.getAttribute("guestId"));
        if (guestId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // guestId를 통해 Guest 객체를 데이터베이스에서 찾기
        Optional<Guest> guest = guestRepository.findById(guestId);
        return guest.orElseThrow(() -> new IllegalStateException("로그인한 사용자를 찾을 수 없습니다."));
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

        if (host == null) {
            List<Reservation> reservations = reservationRepository.findByGuest_Id(guest); // findByGuest_Id로 수정
            model.addAttribute("reservations", reservations);
        }
        if (guest == null) {
            List<Reservation> reservations = reservationRepository.findByHost_Id(host); // findByHost_Id로 수정
            model.addAttribute("reservations", reservations);
        }
        return "mypage/reservationStatus"; // 예약 목록을 보여주는 페이지
    }

    // 예약 추가 처리
    @PostMapping("/reservation")
    public String addReservation(HttpSession session,
                                 @RequestParam("roomId") Long roomId,
                                 @RequestParam("checkinDate") String checkinDate,
                                 @RequestParam("stayDays") int stayDays) {

        // 세션에서 로그인된 Guest 객체 가져오기
        Guest guest = getGuestFromSession(session);

        // Reservation 객체 생성
        Reservation reservation = new Reservation();
        reservation.setGuest(guest); // 로그인한 Guest와 예약 연결
        reservation.setAccommodationRoomId(roomId); // 선택한 객실 ID 설정
        reservation.setDate(checkinDate); // 체크인 날짜 설정
        reservation.setPeople(String.valueOf(stayDays)); // 숙박 기간을 'people' 필드에 저장

        // 예약 저장
        reservationRepository.save(reservation);

        // 예약 목록 페이지로 리다이렉트
        return "redirect:/reservationStatus";
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
