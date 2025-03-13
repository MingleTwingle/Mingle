package com.example.mingle.reservation.controller;

import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.accommodation.repository.AccommodationRoomRepository;
import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;
import com.example.mingle.reservation.domain.Reservation;
import com.example.mingle.reservation.repository.ReservationRepository;
import com.example.mingle.reservation.service.ReservationService;
import com.example.mingle.restaurant.domain.Restaurant;
import com.example.mingle.restaurant.repository.RestaurantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;

    private final GuestRepository guestRepository;

    private final AccommodationRoomRepository accommodationRoomRepository; // AccommodationRoomRepository 주입

    private final HostRepository hostRepository; // HostRepository 주입
    private final RestaurantRepository restaurantRepository;
    private final ReservationService reservationService;

    public ReservationController(ReservationRepository reservationRepository, GuestRepository guestRepository, AccommodationRoomRepository accommodationRoomRepository, HostRepository hostRepository, RestaurantRepository restaurantRepository, ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.accommodationRoomRepository = accommodationRoomRepository;
        this.hostRepository = hostRepository;
        this.restaurantRepository = restaurantRepository;
        this.reservationService = reservationService;
    }

    // 공통적으로 사용되는 guestId를 가져오는 메소드
    private Guest getGuestFromSession(HttpSession session) {
        Long guestId = (Long) session.getAttribute("guestId");
        System.out.println("세션 사용자 아이디: " + guestId);
        if (guestId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // guestId를 통해 Guest 객체를 데이터베이스에서 찾기
        Optional<Guest> guest = guestRepository.findById(guestId);
        return guest.orElseThrow(() -> new IllegalStateException("로그인한 사용자를 찾을 수 없습니다."));
    }

    private Host getHostFromSession(HttpSession session) {
        Long hostId = (Long) session.getAttribute("hostId");
        if (hostId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        return hostRepository.findById(hostId)
                .orElseThrow(() -> new IllegalStateException("로그인한 호스트를 찾을 수 없습니다."));
    }


    // 예약 현황 조회
    @GetMapping("/reservationStatus")
    public String getReservations(HttpSession session, Model model) {
        Long guestId = (Long) session.getAttribute("guestId");
        Long hostId = (Long) session.getAttribute("hostId");

        // guestId를 사용하여 관련 예약 정보 및 레스토랑 이름을 가져옴
        List<Reservation> reservations = reservationRepository.findByGuest_Id(guestId); // 예약 리스트 가져오기
        model.addAttribute("reservations", reservations);

        // 각 예약에 대해 레스토랑 이름을 추출
        // 각 예약에 대해 레스토랑/숙소 이름을 추출
        List<String> restaurantNames = new ArrayList<>();
        List<String> accommodationNames = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> newTime = new ArrayList<>();
        for (Reservation reservation : reservations) {
            // 숙소가 있을 경우 이름을 추가
            String[] dateTimeParts = reservation.getDate().split("T"); // "T" 기준으로 분리
            String dateTime = reservation.getDate();
            if (dateTime.contains("T")) { // "T"가 포함된 경우
                String timePart = dateTime.split("T")[1].split(":")[0] + ":" + dateTime.split("T")[1].split(":")[1]; // HH:mm 추출
                newTime.add(timePart);
            } else {
                newTime.add("없음"); // "T"가 없는 경우 기본값 설정 (예: 00:00)
            }
            date.add(reservation.getDate().split("T")[0]);

            if(reservation.getAccommodationRoom() != null) {
                accommodationNames.add(reservation.getAccommodationRoom().getName()); // 숙소 이름 추가
                restaurantNames.add(null); // 레스토랑 이름은 null
            } else if(reservation.getRestaurant() != null) {
                restaurantNames.add(reservation.getRestaurant().getRestaurantName()); // 레스토랑 이름 추가
                accommodationNames.add(null); // 숙소 이름은 null
            }
        }
        model.addAttribute("newTime", newTime);
        model.addAttribute("dated", date);
        model.addAttribute("restaurantNames", restaurantNames);
        model.addAttribute("accommodationNames", accommodationNames);
        System.out.println("===========================");
        System.out.println(restaurantNames);
        System.out.println(accommodationNames);

        System.out.println("===========================");
        return "mypage/reservationStatus";
    }

    // 예약 추가 처리
    @PostMapping("/reservation")
    public String addReservation(HttpSession session,
                                 @RequestParam("roomId") Long roomId,
                                 @RequestParam("checkinDate") String checkinDate,
                                 @RequestParam("stayDays") int stayDays,
                                 Model model) {

        // 세션에서 로그인된 Guest 객체 가져오기
        Guest guest = getGuestFromSession(session);

        // AccommodationRoom 객체 가져오기
        AccommodationRoom accommodationRoom = accommodationRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 객실을 찾을 수 없습니다. ID: " + roomId));

        // 해당 객실과 날짜에 예약이 이미 있는지 확인
        List<Reservation> existingReservations = reservationRepository.findByAccommodationRoomAndDate(accommodationRoom, checkinDate);
        if (!existingReservations.isEmpty()) {
            // 예약이 이미 있는 경우, 오류 메시지를 전달
            model.addAttribute("errorMessage", "해당 날짜에 이 객실은 이미 예약되었습니다.");
            return ""; // 숙소 상세 페이지로 리다이렉트
        }

        // 객실 이름을 가져오기
        String accommodationRoomName = (accommodationRoom.getAccommodation().getName() != null) ? accommodationRoom.getAccommodation().getName() : "없음";

        // Reservation 객체 생성
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setAccommodationRoomName(accommodationRoomName);
        reservation.setAccommodationRoom(accommodationRoom);
        reservation.setDate(checkinDate);
        reservation.setPeople(String.valueOf(stayDays));

        // 예약 저장
        reservationRepository.save(reservation);

        return "redirect:/reservationStatus"; // 예약이 정상적으로 추가되었을 때
    }




    // 예약 수정 페이지
    @GetMapping("/reservations/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. ID: " + id));

        model.addAttribute("reservation", reservation);
        return "/mypage/reservationEdit";
    }

    // 예약 수정 처리
    @PostMapping("/reservations/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        existingReservation.setDate(reservation.getDate());
        existingReservation.setPeople(reservation.getPeople());
        existingReservation.setCancel(reservation.getCancel());
        existingReservation.setRestaurant(reservation.getRestaurant());
        existingReservation.setAccommodationRoom(reservation.getAccommodationRoom());

        reservationRepository.save(existingReservation);

        return "redirect:/reservationStatus";
    }

    // 예약 삭제
    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable Long id, HttpSession session) {
        Guest guest = getGuestFromSession(session);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getGuest().getId().equals(guest.getId())) {
            return "redirect:/reservationStatus";
        }

        reservationRepository.delete(reservation);
        return "redirect:/reservationStatus";
    }
}
