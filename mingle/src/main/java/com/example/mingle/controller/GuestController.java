package com.example.mingle.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GuestController {
    private final GuestService guestService;


    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html 뷰 반환
    }

    // Spring Security가 자동으로 로그인 검증을 처리하므로 직접 로그인 검증 코드 제거

    // 로그아웃 처리 (Spring Security가 자동으로 처리하지만, 로그아웃 후 메시지 전달 가능)
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃되었습니다.");
        return "redirect:/login";
    }

    // 로그인된 사용자 확인 (테스트용)
    @GetMapping("/user")
    public String getCurrentUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());
        return "user"; // user.html 페이지에서 로그인된 사용자 정보 출력 가능
    }

    // About 페이지
    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }

    // Contact 페이지
    @GetMapping("/contact")
    public String showContact() {
        return "contact";
    }



    // 호스트 등록 페이지
    @GetMapping("/host/register")
    public String showHostRegisterForm() {
        return "host/register";
    }

    // 게스트 or 호스트 선택 페이지
    @GetMapping("/guestOrHost")
    public String showGuestOrHost() {
        return "guestOrHost";
    }

    // 게스트 회원가입 폼
    @GetMapping("/guests/register")
    public String createRegisterForm() {
        return "guest/register";
    }

    // 게스트 회원가입 처리
    @PostMapping("/guests/register")
    public String create(@Validated GuestForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "guest/register";
        }

        Guest guest = new Guest();
        guest.setName(form.getGuest_name());
        guest.setIdid(form.getGuest_idid());
        guest.setNickname(form.getGuest_nickname());
        guest.setPassword(form.getGuest_password()); // 암호화는 GuestService에서 처리
        guest.setEmail(form.getGuest_email());
        guest.setGender(form.getGuest_gender());
        guest.setPhone(form.getGuest_phone_number());

        try {
            guestService.join(guest); // 암호화된 비밀번호로 회원가입
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다! 로그인하세요.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            result.rejectValue("idid", "error.guest", "이미 존재하는 아이디입니다.");
            return "guest/register";
        }


    }

    // 게스트 리스트 조회
    @GetMapping("/guests/list")
    public String list(Model model) {
        List<Guest> guests = guestService.findUser();
        model.addAttribute("guests", guests);
        return "guest/guestList";
    }
}
