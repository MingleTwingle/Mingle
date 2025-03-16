package com.example.mingle.controller;

import com.example.mingle.domain.Couple;
import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.repository.CoupleRepository;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.review.service.ReviewService;
import com.example.mingle.security.CustomUserDetails;
import com.example.mingle.service.CoupleService;
import com.example.mingle.service.GuestService;
import com.example.mingle.service.HostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class MyPageController {

    private GuestService guestService;
    private final CoupleService coupleService;
    private HostService hostService;
    private final GuestRepository guestRepository;
    private final CoupleRepository coupleRepository;

    public MyPageController(GuestService guestService, HostService hostService, CoupleService coupleService, GuestRepository guestRepository, CoupleRepository coupleRepository) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.coupleService = coupleService;
        this.guestRepository = guestRepository;
        this.coupleRepository = coupleRepository;
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
        // ✅ 현재 로그인된 사용자 가져오기
        String username = getCurrentUsername();
        System.out.println(username);
        System.out.println(userDetails.getUsername());
        if (username == null) {
            System.out.println("1");
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 이동
        }

        if (userDetails == null) {
            System.out.println("2");
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }

        System.out.println(" 로그인된 사용자: " + userDetails.getUsername());
        System.out.println(" 사용자 역할: " + userDetails.getRole());

        model.addAttribute("user", userDetails);

        // ✅ 사용자 정보에서 커플 코드 가져오기
        Guest guest = guestRepository.findByIdid(username).orElse(null);
        if (guest == null) {
            System.out.println("3");
            return "redirect:/login";
        }
        String myCoupleCode = guest.getCoupleCode();
        boolean isMatched = guest.isMatched();
        model.addAttribute("coupleCode", myCoupleCode);
        model.addAttribute("isMatched", isMatched);
        Couple couple = coupleRepository.findByGuestId(guest.getId());
        if (couple == null) {
            return "mypage/guest";
        }
        String partnerCoupleCode;
        String guest1Name;
        String guest2Name;
        if (couple.getGuest1().getId().equals(guest.getId())) {
            partnerCoupleCode = couple.getGuest2().getCoupleCode();
            guest1Name = coupleService.getGuest1Name(myCoupleCode);
            guest2Name = coupleService.getGuest2Name(partnerCoupleCode);
        } else {
            partnerCoupleCode = couple.getGuest1().getCoupleCode();
            guest1Name = coupleService.getGuest2Name(myCoupleCode);
            guest2Name = coupleService.getGuest1Name(partnerCoupleCode);
        }

        model.addAttribute("guest1Name", guest1Name);
        model.addAttribute("guest2Name", guest2Name);

        return "mypage/guest";
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return null;
//        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
//
//        if (userDetails == null) {
//            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
//        }
//
//        // userDetails에서 아이디를 가져와 Guest 찾기
//        Guest guest = guestService.findByIdid(userDetails.getUsername());
//        if (guest != null) {
//            model.addAttribute("coupleCode", guest.getCoupleCode()); // 커플 코드 추가
//        }
//
//        System.out.println(" 로그인된 사용자: " + userDetails.getUsername());
//        System.out.println(" 사용자 역할: " + userDetails.getRole());
//
//        model.addAttribute("user", userDetails);    // html guest 정보를 활용할 수 있게 model에 저장
//        return "mypage/guest"; //  guest.html로 연결
//    }

    @GetMapping("/mypage/host")
    public String hostMyPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }
        model.addAttribute("user", userDetails);
        return "mypage/host"; //  host.html로 연결
    }

    //회원 정보  누르면 회원 정보 페이지로 넘어가기~
    @GetMapping("/mypage/profile")
    public String guestProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }

        Guest guest = guestService.findByIdid(userDetails.getUsername());
        if (guest != null) {
            model.addAttribute("guest", guest); // Guest 정보를 모델에 추가
        }

        return "mypage/profile"; // profile.html로 이동
    }

    // 회원 탈퇴 기능
    @PostMapping("/mypage/delete")
    public String deleteGuest(@AuthenticationPrincipal CustomUserDetails userDetails,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        if(userDetails == null) {
            return "redirect:/login";
        }

        String role = userDetails.getRole();   // guest인지 host인지

        if("ROLE_USER".equals(role)) {
            Guest guest = guestService.findByIdid(userDetails.getUsername());
            if(guest != null) {
                guestService.deleteGuestById(guest.getId());
            }
        } else if("ROLE_HOST".equals(role)) {
            Host host = hostService.findByIdid(userDetails.getUsername());
            if(host != null) {
                hostService.deleteHostById(host.getId());
            }
        }
        // ✅ 자동 로그아웃 처리 (세션 무효화)
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);

        return "redirect:/"; // 탈퇴하면 메인 페이지로 이동
    }
}

