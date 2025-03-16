package com.example.mingle.accommodation.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.accommodation.service.AccommodationRoomService;
import com.example.mingle.accommodation.service.AccommodationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final AccommodationRoomService accommodationRoomService;

    @Value("classpath:/static/")
    private String imageBasePath;

    public AccommodationController(AccommodationService accommodationService, AccommodationRoomService accommodationRoomService) {
        this.accommodationService = accommodationService;
        this.accommodationRoomService = accommodationRoomService;

    }

    @GetMapping("/accommodation/new")
    public String createForm() {
        return "accommodation/register";
    }

    @PostMapping("/accommodation/new")
    public String create(@Validated AccommodationForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "accommodation/register";
        }
        Accommodation accommodation = new Accommodation();
        accommodation.setName(form.getName());
        accommodation.setLocation(form.getLocation());
        accommodation.setParkingAvailable(form.isParkingAvailable());
        accommodation.setMorningAvailable(form.isMorningAvailable());
        accommodation.setCheckInTime(form.getCheckInTime());
        accommodation.setCheckOutTime(form.getCheckOutTime());
        accommodationService.join(accommodation);
        return "redirect:/";
    }

    @GetMapping("/accommodation/list")
    public String listAll(Model model) {
        List<Accommodation> accommodations = accommodationService.findAccommodation();
        model.addAttribute("accommodations", accommodations);  // ✅ 변수명 수정
        return "accommodation/accommodationList";
    }


    @GetMapping("/accommodation/filter")
    public String filterList(Model model) {
        List<Accommodation> accommodations = accommodationService.findAccommodation();
        model.addAttribute("accommodation", accommodations);
        return "accommodation/accommodationFilter";
    }

    @PostMapping("/accommodation/filter")
    public String customFilter(@Validated AccommodationFilterForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "accommodation/accommodationFilter";
        }

        List<Accommodation> filteredAccommodations;

        if (isFilterEmpty(form)) {
            filteredAccommodations = accommodationService.findAccommodation(); // 모든 숙소 반환
            log.info("입력값 없음 -> 전체 숙소 반환: " + filteredAccommodations.size());
        } else {
            filteredAccommodations = accommodationService.searchAccommodation(form.getLocation(), form.getCheckInTime(), form.getCheckOutTime());
            log.info("필터링된 숙소 개수: " + filteredAccommodations.size());
        }

        model.addAttribute("accommodations", filteredAccommodations);
        return "accommodation/accommodationFilterList";
    }

    private boolean isFilterEmpty(AccommodationFilterForm form) {
        return (form.getLocation() == null || form.getLocation().isBlank()) &&
                (form.getCheckInTime() == null) &&
                (form.getCheckOutTime() == null);
    }

    @GetMapping("/accommodation/filterList")
    public String showFilterList(Model model) {
        List<Accommodation> accommodations = accommodationService.findAccommodation();
        model.addAttribute("accommodations", accommodations);
        return "accommodation/accommodationFilterList";
    }

    // 🔹 숙소 상세 페이지 조회 (이전 페이지 기억 추가)
    @GetMapping("/accommodationDetail/{id}")
    public String showAccommodationDetail(@PathVariable("id") Long id, Model model, HttpServletRequest request, HttpSession session) {
        log.info("요청된 숙소 ID: " + id);  // ✅ 디버깅용 로그 추가

        // 🔹 이전 페이지 URL을 세션에 저장 (현재 페이지가 이전 페이지가 아닐 경우만 저장)
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/accommodationDetail/" + id)) {
            session.setAttribute("prevPage", referer);
        }

        // 숙소 정보 가져오기 (변수 선언 및 초기화)
        Accommodation accommodation = accommodationService.findById(id);
        if (accommodation == null) {
            return "redirect:/accommodation/filterList";  // 숙소 정보가 없으면 검색 페이지로 이동
        }
        List<AccommodationRoom> roomList = accommodationRoomService.getRoomsByAccommodationId(id);

        String imageFolderPath = imageBasePath.replace("file:", "") + "ac";
        File folder = new File(imageFolderPath);
        Map<Long, String> roomPhotosMap = new HashMap<>();
        for (AccommodationRoom room : roomList) {
            String imagePath = "/images/ac/ac" + room.getId() + ".jpg";
            roomPhotosMap.put(room.getId(), imagePath);
        }


        // 모델에 숙소 정보 추가
        model.addAttribute("accommodation", accommodation);
        model.addAttribute("roomList", roomList);
        model.addAttribute("roomPhotosMap", roomPhotosMap );
        session.setAttribute("accommodationId", id); // ✅ guest_id 저장

        return "accommodation/accommodationDetail";  // ✅ 올바른 View 반환
    }

    @PostMapping("/accommodationDetail/{id}")
    public String showAccommodationDetail1(@PathVariable("id") Long id, Model model, HttpSession session) {
        log.info("요청된 숙소 ID: " + id);  // ✅ 디버깅용 로그 추가
        if (session != null) {
            session.setAttribute("accommodationId", id);  // ✅ 세션이 있을 경우에만 저장
        }

        // 숙소 정보 가져오기 (변수 선언 및 초기화)
        Accommodation accommodation = accommodationService.findById(id);
        if (accommodation == null) {
            throw new RuntimeException("숙소 정보를 찾을 수 없습니다. ID: " + id);
        }
        List<AccommodationRoom> roomList = accommodationRoomService.getRoomsByAccommodationId(id);

        String imageFolderPath = imageBasePath.replace("file:", "") + "ac";
        File folder = new File(imageFolderPath);
        Map<Long, String> roomPhotosMap = new HashMap<>();
        for (AccommodationRoom room : roomList) {
            String imagePath = "/images/ac/ac" + room.getId() + ".jpg";
            roomPhotosMap.put(room.getId(), imagePath);
        }


        // 모델에 숙소 정보 추가
        model.addAttribute("accommodation", accommodation);
        model.addAttribute("roomList", roomList);
        model.addAttribute("roomPhotosMap", roomPhotosMap );

        return "accommodation/accommodationDetail";  // ✅ 올바른 View 반환
    }

}
