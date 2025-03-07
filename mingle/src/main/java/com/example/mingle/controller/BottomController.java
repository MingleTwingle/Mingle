package com.example.mingle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BottomController {

    @GetMapping("/bottom/careers/careers")
    public String showCareersForm() {
        return "/bottom/careers/careers"; // Thymeleaf 템플릿 파일 (resources/templates/careers/apply.html)
    }

    @GetMapping("/bottom/careers/apply")
    public String showApplyForm() {
        return "/bottom/careers/apply";
    }

    @PostMapping("/bottom/careers/apply")
    public String submitApplication(@RequestParam("name") String name,
                                    @RequestParam("email") String email,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("job") String job,
                                    @RequestParam("cover_letter") String coverLetter,
                                    @RequestParam("resume") MultipartFile resume) {
        // 여기서 지원서 저장 로직 구현
        System.out.println("지원 완료: " + name + " - " + email);
        return "redirect:/";
    }

    @GetMapping("/bottom/press")
    public String press() {
        return "bottom/press";  // templates/bottom/press.html로 이동
    }

    @GetMapping("/bottom/packages")
    public String packages() {
        return "bottom/packages";  // templates/bottom/packages.html로 이동
    }

    @GetMapping("/bottom/gift-cards")
    public String giftCards() {
        return "bottom/gift-cards";  // templates/bottom/gift-cards.html로 이동
    }

    @GetMapping("/bottom/help")
    public String help() {
        return "bottom/help";  // templates/bottom/help.html로 이동
    }

    @GetMapping("/bottom/faq")
    public String faq() {
        return "bottom/faq";  // templates/bottom/faq.html로 이동
    }

    @GetMapping("/bottom/partners")
    public String partners() {
        return "bottom/partners";  // templates/bottom/partners.html로 이동
    }

    @GetMapping("/bottom/terms")
    public String terms() {
        return "bottom/terms";  // templates/bottom/terms.html로 이동
    }

    @GetMapping("/bottom/privacy")
    public String privacy() {
        return "bottom/privacy";  // templates/bottom/privacy.html로 이동
    }

    @GetMapping("/bottom/cookies")
    public String cookies() {
        return "bottom/cookies";  // templates/bottom/cookies.html로 이동
    }
}
