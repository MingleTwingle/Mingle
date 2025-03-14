package com.example.mingle.reservation.controller;

import com.example.mingle.accommodation.domain.Accommodation;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

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


    @GetMapping("/reservationStatus")
    public String getReservations(HttpSession session, Model model) {
        Long guestId = (Long) session.getAttribute("guestId");
        Long hostId = (Long) session.getAttribute("hostId");

        // guestId를 사용하여 관련 예약 정보 및 레스토랑 이름을 가져옴
        List<Reservation> reservations = reservationRepository.findByGuest_Id(guestId); // 예약 리스트 가져오기
        model.addAttribute("reservations", reservations);

        // 각 예약에 대해 레스토랑 이름을 추출
        List<String> restaurantNames = new ArrayList<>();
        List<String> accommodationNames = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> newTime = new ArrayList<>();

        for (Reservation reservation : reservations) {
            String dateTime = reservation.getDate(); // 예약 날짜 가져오기
            newTime.add(reservation.getNewTime());

            if (reservation.getAccommodationRoom() != null) {
                accommodationNames.add(reservation.getAccommodationRoom().getName()); // 숙소 이름 추가
                restaurantNames.add(null); // 레스토랑 이름은 null
                date.add(dateTime.split("T")[0]);

            } else if (reservation.getRestaurant() != null) {
                restaurantNames.add(reservation.getRestaurant().getRestaurantName()); // 레스토랑 이름 추가
                accommodationNames.add(null); // 숙소 이름은 null
                date.add(dateTime.split("T")[0]);
            }
        }


        model.addAttribute("newTime", newTime);
        model.addAttribute("dated", date);
        model.addAttribute("restaurantNames", restaurantNames);
        model.addAttribute("accommodationNames", accommodationNames);

        System.out.println("===========================");
        System.out.println(restaurantNames);
        System.out.println(accommodationNames);
        System.out.println(newTime);
        System.out.println(date);
        System.out.println("===========================");

        return "mypage/reservationStatus";
    }


    @PostMapping("/reservation/accommodation")
    public String addReservationAcc(HttpSession session,
                                    @RequestParam("roomId" ) Long roomId,
                                    @RequestParam("checkinDate") String checkinDate,
                                    @RequestParam("stayDays") int stayDays,
                                    @RequestParam("checkinTime") String checkinTime,
                                    Model model) {
        // 세션에서 로그인된 Guest 객체 가져오기
        Guest guest = getGuestFromSession(session);
        String peopleNum = "2";
        Long accommodationId = (Long) session.getAttribute("accommodationId");

        // AccommodationRoom 객체 가져오기
        AccommodationRoom accommodationRoom = accommodationRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 객실을 찾을 수 없습니다. ID: " + roomId));

        // 날짜 형식을 "yyyy-MM-dd"로 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(accommodationRoom);
        // checkinDate를 LocalDate로 변환
        LocalDate startDate = LocalDate.parse(checkinDate, formatter);

        // stayDays만큼 더해서 종료 날짜 계산
        LocalDate endDate = startDate.plusDays(stayDays - 1);

        // 기존 예약 목록 가져오기
        List<Reservation> existingReservations = reservationRepository.findByAccommodationRoom(accommodationRoom);

        // 날짜 범위가 겹치는지 확인
        for (Reservation existingReservation : existingReservations) {
            String[] dateRange = existingReservation.getDate().split("~");

            if (dateRange.length == 2) {
                LocalDate existingStartDate = LocalDate.parse(dateRange[0], formatter);
                LocalDate existingEndDate = LocalDate.parse(dateRange[1], formatter);
                System.out.println(existingStartDate);
                System.out.println(existingEndDate);
                System.out.println(existingStartDate.isBefore(existingEndDate));
                System.out.println(existingStartDate.isAfter(existingEndDate));
                // 날짜 범위가 겹치는지 확인
                if ((startDate.isBefore(existingEndDate) || startDate.isEqual(existingEndDate)) &&
                        (endDate.isAfter(existingStartDate) || endDate.isEqual(existingStartDate))) {
                    model.addAttribute("errorMessage", "해당 날짜에 이 객실은 이미 예약되었습니다.");
                    return "forward:/accommodationDetail/" + accommodationId;  // 오류 발생 시 상세 페이지로 리다이렉트
                }

            } else {
                model.addAttribute("errorMessage", "잘못된 예약 날짜 범위입니다.");
                return "forward:/accommodationDetail/" + accommodationId;  // 날짜 범위 오류 시 리다이렉트
            }

        }

        // 객실 이름 가져오기
        System.out.println(accommodationRoom.getAccommodation().getName());

        String accommodationRoomName = (accommodationRoom.getAccommodation().getName() != null) ? accommodationRoom.getAccommodation().getName() : "없음";

        // 결과 날짜 범위 생성
        String dateRange = startDate.format(formatter) + "~" + endDate.format(formatter);

        // Reservation 객체 생성
        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setAccommodationRoomName(accommodationRoomName);
        reservation.setAccommodationRoom(accommodationRoom);
        reservation.setDate(dateRange);
        reservation.setPeople(peopleNum);
        reservation.setNewTime(String.valueOf(checkinTime));

        // 예약 저장
        reservationRepository.save(reservation);

        return "redirect:/reservationStatus"; // 예약이 정상적으로 추가되었을 때
    }


    @PostMapping("/reservation/restaurant")
    public String addReservationRes(HttpSession session,
                                    @RequestParam("restaurantId") Long restId,
                                    @RequestParam("reservationDate") String resDate,
                                    @RequestParam("reservationTime") String reservationTime,
                                    Model model) {

        System.out.println("===============================");
        System.out.println("Received restaurantId: " + restId);
        System.out.println("Reservation Date: " + resDate);
        System.out.println("Reservation Time: " + reservationTime);

        // 세션에서 로그인된 Guest 객체 가져오기
        Guest guest = getGuestFromSession(session);

        // Restaurant 객체 가져오기
        Restaurant restaurant = restaurantRepository.findById(restId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식당을 찾을 수 없습니다. ID: " + restId));

        // reservationTime의 공백을 T로 교체하여 "yyyy-MM-dd hh:mm" -> "yyyy-MM-dd'T'HH:mm" 형식으로 수정
        String formattedReservationTime = reservationTime.replace(" ", "T");  // 공백을 T로 변경

        // 예약 날짜와 시간을 결합할 때 T가 하나만 들어가도록 수정
        String dateTimeString = resDate + "T" + formattedReservationTime;  // "yyyy-MM-dd'T'HH:mm" 형식으로 결합

        System.out.println(dateTimeString);
        System.out.println(resDate);
        System.out.println(reservationTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");  // 날짜와 시간 모두 포함된 포맷

        try {
            // 예약 날짜와 시간 파싱
            LocalDateTime reservationDateTime = LocalDateTime.parse(dateTimeString, formatter);

            // 기존 예약 목록 가져오기
            List<Reservation> existingReservations = reservationRepository.findByRestaurant(restaurant);

            // 날짜와 시간 겹치는지 확인
            for (Reservation existingReservation : existingReservations) {
                // 기존 예약 시간도 T 포함된 형식으로 비교
                if (existingReservation.getAccommodationRoom() == null) {
                    String existingDateTimeString = existingReservation.getDate() + "T" + existingReservation.getNewTime();
                    LocalDateTime existingDateTime = LocalDateTime.parse(existingDateTimeString, formatter);

                    System.out.println("=============================");
                    System.out.println(existingReservation.getDate());
                    System.out.println(existingDateTime);
                    System.out.println(reservationDateTime);
                    System.out.println("=============================");

                    // 예약 시간 중복 체크
                    if (existingDateTime.equals(reservationDateTime)) {
                        model.addAttribute("errorMessage", "해당 날짜와 시간에 이미 예약이 있습니다.");
                        System.out.println("==============================");
                        System.out.println(restId);
                        System.out.println("==============================");
                        return "forward:/restaurants/" + restId;// 오류 발생 시 상세 페이지로 리다이렉트
                    }
                }
            }

            // 레스토랑 이름 설정
            String restaurantName = restaurant.getRestaurantName() != null ? restaurant.getRestaurantName() : "없음";
            System.out.println("reservationTime: " + reservationTime);
            System.out.println("reservationDate: " + resDate);

            // Reservation 객체 생성
            Reservation reservation = new Reservation();
            reservation.setGuest(guest);
            reservation.setRestaurantName(restaurantName);
            reservation.setRestaurant(restaurant);

            // newTime은 예약 시간만 (hh:mm) 저장
            reservation.setNewTime(reservationDateTime.toLocalTime().toString());  // "hh:mm"

            // date는 예약 날짜만 (yyyy-MM-dd) 저장
            reservation.setDate(reservationDateTime.toLocalDate().toString());  // "yyyy-MM-dd"

            reservation.setPeople("2");  // 기본값 설정

            // 예약 저장
            reservationRepository.save(reservation);

        } catch (DateTimeParseException e) {
            model.addAttribute("errorMessage", "잘못된 날짜와 시간 형식입니다.");
            return "/restaurants/" + restId;  // 오류 발생 시 상세 페이지로 리다이렉트
        }

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
        System.out.println("=========================");
        System.out.println(id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getGuest().getId().equals(guest.getId())) {
            return "redirect:/reservationStatus";
        }

        reservationRepository.delete(reservation);
        return "redirect:/reservationStatus";
    }
}
