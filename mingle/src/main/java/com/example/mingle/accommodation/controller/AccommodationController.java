package com.example.mingle.accommodation.controller;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.accommodation.service.AccommodationRoomService;
import com.example.mingle.accommodation.service.AccommodationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final AccommodationRoomService accommodationRoomService;

    @Value("classpath:/static/")
    private String imageBasePath;

    @Autowired
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
            System.out.println("입력값 없음 -> 전체 숙소 반환: " + filteredAccommodations.size());
        } else {
            filteredAccommodations = accommodationService.searchAccommodation(form.getLocation(), form.getCheckInTime(), form.getCheckOutTime());
            System.out.println("필터링된 숙소 개수: " + filteredAccommodations.size());
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

    @GetMapping("/accommodationDetail/{id}")
    public String showAccommodationDetail(@PathVariable("id") Long id, Model model, HttpSession session) {
        System.out.println("요청된 숙소 ID: " + id);  // ✅ 디버깅용 로그 추가
        session.setAttribute("accommodationId", id); // ✅ guest_id 저장
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
