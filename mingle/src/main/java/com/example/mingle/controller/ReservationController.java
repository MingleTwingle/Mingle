package com.example.mingle.controller;

import com.example.mingle.domain.Reservation;
import com.example.mingle.repository.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;

    // 생성자를 통한 Repository 주입
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    public String getReservations(Model model) {
        // 데이터베이스에서 예약 데이터 가져오기
        List<Reservation> reservations = reservationRepository.findAll();

        // 모델에 데이터 추가
        model.addAttribute("reservations", reservations);
        return "reservationList"; // Thymeleaf 파일명 (templates/reservationList.html)
    }
}
