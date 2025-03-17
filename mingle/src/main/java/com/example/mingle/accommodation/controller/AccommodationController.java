package com.example.mingle.accommodation.controller;

import com.example.mingle.accommodation.domain.Accommodation;
import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.accommodation.service.AccommodationRoomService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.mingle.accommodation.service.AccommodationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String customFilter(@Validated AccommodationFilterForm form, BindingResult result, RedirectAttributes redirectAttributes,Model model) {
        if (result.hasErrors()) {
            return "redirect:/accommodation/filter";  // 🔹 에러 발생 시 필터 페이지로 리다이렉트
        }

        List<Accommodation> filteredAccommodations;

        if (isFilterEmpty(form)) {
            filteredAccommodations = accommodationService.findAccommodation();
        } else {
            filteredAccommodations = accommodationService.searchAccommodation(
                    form.getLocation(), form.getCheckInTime(), form.getCheckOutTime()
            );
        }
        redirectAttributes.addFlashAttribute("accommodations", filteredAccommodations); // 🔹 Flash Attribute 사용
        model.addAttribute("accommodations", filteredAccommodations);
        return "redirect:/accommodation/filterList";  // 🔹 GET 요청으로 필터 리스트 페이지 이동
    }


    private boolean isFilterEmpty(AccommodationFilterForm form) {
        return (form.getLocation() == null || form.getLocation().isBlank()) &&
                (form.getCheckInTime() == null) &&
                (form.getCheckOutTime() == null);
    }

    @GetMapping("/accommodation/filterList")
    public String showFilteredAccommodations(@ModelAttribute("accommodations") List<Accommodation> accommodations, Model model) {
        model.addAttribute("accommodations", accommodations);  // 다시 넣어줘야 화면에서 사용 가능
        return "accommodation/accommodationFilterList";
    }


    @GetMapping("/accommodationDetail/{id}")
    public String showAccommodationDetail(@PathVariable("id") Long id, Model model, HttpServletRequest request, HttpSession session) {
        System.out.println("요청된 숙소 ID: " + id);  // ✅ 디버깅 로그 추가

        // 🔹 이전 페이지 URL을 세션에 저장 (현재 페이지가 이전 페이지가 아닐 경우만 저장)
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/accommodationDetail/")) {
            session.setAttribute("prevPage", referer);  // 🔹 세션에 이전 페이지 저장
        } else {
            session.setAttribute("prevPage", "/accommodation/filterList");  // 기본값 설정
        }

        // 숙소 정보 가져오기
        Accommodation accommodation = accommodationService.findById(id);
        if (accommodation == null) {
            return "redirect:/accommodation/filterList";  // 숙소 정보가 없으면 검색 페이지로 이동
        }
        List<AccommodationRoom> roomList = accommodationRoomService.getRoomsByAccommodationId(id);

        // 이미지 맵 초기화
        Map<Long, String> roomPhotosMap = new HashMap<>();
        for (AccommodationRoom room : roomList) {
            String imagePath = "/images/ac/ac" + room.getId() + ".jpg";
            roomPhotosMap.put(room.getId(), imagePath);
        }

        // 모델에 데이터 추가
        model.addAttribute("accommodation", accommodation);
        model.addAttribute("roomList", roomList);
        model.addAttribute("roomPhotosMap", roomPhotosMap);
        session.setAttribute("accommodationId", id); // ✅ 세션에 숙소 ID 저장

        return "accommodation/accommodationDetail";  // ✅ 올바른 View 반환
    }


    @PostMapping("/accommodationDetail/{id}")
    public String showAccommodationDetail1(@PathVariable("id") Long id, Model model, HttpSession session) {
        System.out.println("요청된 숙소 ID: " + id);  // ✅ 디버깅용 로그 추가
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
