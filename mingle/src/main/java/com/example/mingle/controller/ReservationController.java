/*
package com.example.mingle.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController<Reservation> {

    @Autowired
    private ReservationService reservationService;

    // 1️⃣ 예약 현황 조회
    @GetMapping("/status")
    public String viewReservationStatus(HttpSession session, Model model) {
        Guest loggedInUser = (Guest) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        List<Reservation> reservations = (List<Reservation>) reservationService.getReservationsByGuest(loggedInUser.getId());
        model.addAttribute("reservations", reservations);
        return "mypage/reservationStatus"; // reservationStatus.html 연결
    }

    // 2️⃣ 예약 수정 페이지 로드
    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable Long id, Model model) {
        Reservation reservation = (Reservation) reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/reservation/status?error=not_found";
        }

        model.addAttribute("reservation", reservation);
        return "mypage/reservationEdit"; // reservationEdit.html 연결
    }

    // 3️⃣ 예약 수정 처리
    @PostMapping("/update")
    public String updateReservation(@RequestParam Long reservationId,
                                    @RequestParam String newDate,
                                    @RequestParam String newTime) {
        reservationService.updateReservation(reservationId, newDate, newTime);
        return "redirect:/reservation/status?success=updated";
    }

    // 4️⃣ 예약 취소 페이지 로드
    @GetMapping("/cancel/{id}")
    public String cancelReservationPage(@PathVariable Long id, Model model) {
        Reservation reservation = (Reservation) reservationService.getReservationById(id);
        if (reservation == null) {
            return "redirect:/reservation/status?error=not_found";
        }

        model.addAttribute("reservation", reservation);
        return "/mypage/reservationCancel"; // reservationCancel.html 연결
    }

    // 5️⃣ 예약 취소 처리
    @PostMapping("/cancel")
    public String cancelReservation(@RequestParam Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return "redirect:/reservation/status?success=canceled";
    }
}
*/
