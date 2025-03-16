package com.example.mingle.reservation.controller;

import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.accommodation.repository.AccommodationRoomRepository;
import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.reservation.domain.Reservation;
import com.example.mingle.reservation.repository.ReservationRepository;
import com.example.mingle.restaurant.domain.Restaurant;
import com.example.mingle.restaurant.repository.RestaurantRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final AccommodationRoomRepository accommodationRoomRepository;
    private final RestaurantRepository restaurantRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 GuestRepository guestRepository,
                                 AccommodationRoomRepository accommodationRoomRepository,
                                 RestaurantRepository restaurantRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.accommodationRoomRepository = accommodationRoomRepository;
        this.restaurantRepository = restaurantRepository;
    }
    //delete 메서드
    @DeleteMapping("/reservations/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isPresent()) {
            reservationRepository.deleteById(id);
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("예약을 찾을 수 없습니다.");
        }
    }

    //  공통적으로 사용되는 guestId를 가져오는 메서드 (호스트 예약 불가능)
    private Guest getGuestFromSession(HttpSession session) {
        Long guestId = (Long) session.getAttribute("guestId");
        Long hostId = (Long) session.getAttribute("hostId");

        if (hostId != null) {
            throw new IllegalStateException("호스트 계정은 예약 기능을 사용할 수 없습니다.");
        }
        if (guestId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        return guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalStateException("로그인한 사용자를 찾을 수 없습니다."));
    }

    // 예약 조회 페이지
    @GetMapping("/reservationStatus")
    public String getReservations(HttpSession session, Model model) {
        Long guestId = (Long) session.getAttribute("guestId");

        List<Reservation> reservations = reservationRepository.findByGuest_Id(guestId);
        model.addAttribute("reservations", reservations);

        List<String> restaurantNames = new ArrayList<>();
        List<String> accommodationNames = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> newTime = new ArrayList<>();

        for (Reservation reservation : reservations) {
            String dateTime = reservation.getDate();
            newTime.add(reservation.getNewTime());

            if (reservation.getAccommodationRoom() != null) {
                accommodationNames.add(reservation.getAccommodationRoom().getName());
                restaurantNames.add(null);
                date.add(dateTime.split("T")[0]);
            } else if (reservation.getRestaurant() != null) {
                restaurantNames.add(reservation.getRestaurant().getRestaurantName());
                accommodationNames.add(null);
                date.add(dateTime.split("T")[0]);
            }
        }

        model.addAttribute("newTime", newTime);
        model.addAttribute("dated", date);
        model.addAttribute("restaurantNames", restaurantNames);
        model.addAttribute("accommodationNames", accommodationNames);

        return "mypage/reservationStatus";
    }

    // 숙소 예약
    @PostMapping("/reservation/accommodation")
    public String addReservationAcc(HttpSession session,
                                    @RequestParam("roomId") Long roomId,
                                    @RequestParam("checkinDate") String checkinDate,
                                    @RequestParam("stayDays") int stayDays,
                                    @RequestParam("checkinTime") String checkinTime,
                                    Model model) {
        Guest guest;
        try {
            guest = getGuestFromSession(session);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "forward:/accommodationDetail/" + roomId; // 호스트는 에러
        }

        AccommodationRoom accommodationRoom = accommodationRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 객실을 찾을 수 없습니다. ID: " + roomId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(checkinDate, formatter);
        LocalDate endDate = startDate.plusDays(stayDays - 1);

        List<Reservation> existingReservations = reservationRepository.findByAccommodationRoom(accommodationRoom);

        for (Reservation existingReservation : existingReservations) {
            String[] dateRange = existingReservation.getDate().split("~");
            if (dateRange.length == 2) {
                LocalDate existingStartDate = LocalDate.parse(dateRange[0], formatter);
                LocalDate existingEndDate = LocalDate.parse(dateRange[1], formatter);

                if ((startDate.isBefore(existingEndDate) || startDate.isEqual(existingEndDate)) &&
                        (endDate.isAfter(existingStartDate) || endDate.isEqual(existingStartDate))) {
                    model.addAttribute("errorMessage", "해당 날짜에 이 객실은 이미 예약되었습니다.");
                    return "forward:/accommodationDetail/" + roomId;
                }
            }
        }

        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setAccommodationRoom(accommodationRoom);
        reservation.setDate(startDate + "~" + endDate);
        reservation.setPeople("2");
        reservation.setNewTime(checkinTime);

        reservationRepository.save(reservation);

        return "redirect:/reservationStatus";
    }

    // 레스토랑 예약
    @PostMapping("/reservation/restaurant")
    public String addReservationRes(HttpSession session,
                                    @RequestParam("restaurantId") Long restId,
                                    @RequestParam("reservationDate") String resDate,
                                    @RequestParam("reservationTime") String reservationTime,
                                    Model model) {
        Guest guest;
        try {
            guest = getGuestFromSession(session);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "forward:/restaurants/" + restId;
        }

        Restaurant restaurant = restaurantRepository.findById(restId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당을 찾을 수 없습니다. ID: " + restId));

        String dateTimeString = resDate + "T" + reservationTime.replace(" ", "T");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        try {
            LocalDateTime reservationDateTime = LocalDateTime.parse(dateTimeString, formatter);

            List<Reservation> existingReservations = reservationRepository.findByRestaurant(restaurant);
            for (Reservation existingReservation : existingReservations) {
                String existingDateTimeString = existingReservation.getDate() + "T" + existingReservation.getNewTime();
                LocalDateTime existingDateTime = LocalDateTime.parse(existingDateTimeString, formatter);

                if (existingDateTime.equals(reservationDateTime)) {
                    model.addAttribute("errorMessage", "해당 날짜와 시간에 이미 예약이 있습니다.");
                    return "forward:/restaurants/" + restId;
                }
            }

            Reservation reservation = new Reservation();
            reservation.setGuest(guest);
            reservation.setRestaurant(restaurant);
            reservation.setDate(reservationDateTime.toLocalDate().toString());
            reservation.setNewTime(reservationDateTime.toLocalTime().toString());
            reservation.setPeople("2");

            reservationRepository.save(reservation);
        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "잘못된 날짜와 시간 형식입니다.");
            return "forward:/restaurants/" + restId;
        }

        return "redirect:/reservationStatus";
    }
}
