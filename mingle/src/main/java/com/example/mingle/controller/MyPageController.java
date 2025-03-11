package com.example.mingle.controller;

import com.example.mingle.domain.Guest;
import com.example.mingle.security.CustomUserDetails;
import com.example.mingle.service.GuestService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

    private GuestService guestService;

    public MyPageController(GuestService guestService) {
        this.guestService = guestService;
    }

//    @GetMapping("/mypage/guest")
//    public String showMyPage() {
//        return "mypage/guest";
//    }
//
//
//    @GetMapping("/mypage/host")
//    public String showMyHost() {
//        return "mypage/host";
//    }

    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {

            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }

        System.out.println(" 로그인된 사용자: " + userDetails.getUsername());
        System.out.println(" 사용자 역할: " + userDetails.getRole());

        model.addAttribute("user", userDetails);

        if ("ROLE_HOST".equals(userDetails.getRole())) {
            System.out.println("🔵 호스트 마이페이지로 이동");
            return "/mypage/host"; // /mypage/host로 리다이렉트
        } else {
            System.out.println("🟢 게스트 마이페이지로 이동");
            return "redirect:/mypage/guest"; // /mypage/guest로 리다이렉트
        }
    }

    @GetMapping("/mypage/guest")
    public String guestMyPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }

        // userDetails에서 아이디를 가져와 Guest 찾기
        Guest guest = guestService.findByIdid(userDetails.getUsername());
        if (guest != null) {
            model.addAttribute("coupleCode", guest.getCoupleCode()); // 커플 코드 추가
        }

        System.out.println(" 로그인된 사용자: " + userDetails.getUsername());
        System.out.println(" 사용자 역할: " + userDetails.getRole());

        model.addAttribute("user", userDetails);    // html guest 정보를 활용할 수 있게 model에 저장
        return "mypage/guest"; //  guest.html로 연결
    }

    @GetMapping("/mypage/host")
    public String hostMyPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }
        model.addAttribute("user", userDetails);
        return "mypage/host"; //  host.html로 연결
    }
}
