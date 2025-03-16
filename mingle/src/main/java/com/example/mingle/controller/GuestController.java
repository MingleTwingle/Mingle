package com.example.mingle.controller;

import com.example.mingle.domain.Couple;
import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.repository.CoupleRepository;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.service.CoupleService;
import com.example.mingle.service.GuestService;
import com.example.mingle.service.HostService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class GuestController {
    private final GuestService guestService;
    private final HostService hostService;
    private final CoupleService coupleService;
    private final GuestRepository guestRepository;
    private final CoupleRepository coupleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public GuestController(GuestService guestService, HostService hostService, CoupleService coupleService, GuestRepository guestRepository, CoupleRepository coupleRepository, PasswordEncoder passwordEncoder) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.coupleService = coupleService;
        this.guestRepository = guestRepository;
        this.coupleRepository = coupleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html 뷰 반환
    }

    // Spring Security가 자동으로 로그인 검증을 처리하므로 직접 로그인 검증 코드 제거

    // 로그아웃 처리 (Spring Security가 자동으로 처리하지만, 로그아웃 후 메시지 전달 가능)
    // 로그아웃 처리 (세션 초기화 추가)
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // ✅ 세션 초기화
        redirectAttributes.addFlashAttribute("logoutMessage", "로그아웃되었습니다.");
        return "redirect:/login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // 로그인한 사용자 이름 (이메일)


        System.out.println("로그인된 사용자 이메일: " + username);
        Guest loggedInUser = guestService.findByIdid(username); // ✅ idid로 검색
        Host host = hostService.findByIdid(username);

        if (loggedInUser != null) {
            session.setAttribute("guestId", loggedInUser.getId()); // ✅ guest_id 저장
            System.out.println("로그인된 사용자 아이디: " + loggedInUser.getId());
        } else if (host != null) {
            session.setAttribute("hostId", host.getId()); // ✅ guest_id 저장
            System.out.println("로그인된 사용자 아이디: " + host.getId());
        }
        System.out.println("세션 사용자 아이디: " + (Long) session.getAttribute("guestId"));
        return "redirect:/";
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
//    @GetMapping("/host/register")
//    public String showHostRegisterForm() {
//        return "host/register";
//    }

    // 게스트 or 호스트 선택 페이지
    @GetMapping("/guestOrHost")
    public String showGuestOrHost() {
        return "guestOrHost";
    }

    // 게스트 회원가입 폼
    @GetMapping("/guests/register")
    public String createRegisterForm(Model model) {
        model.addAttribute("guestForm", new GuestForm());
        return "guest/register";
    }

    // 게스트 회원가입 처리
    @PostMapping("/guests/register")
    public String create(@Validated @ModelAttribute("guestForm") GuestForm form, BindingResult result, RedirectAttributes redirectAttributes) {
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
            guestService.join(guest, result); // 암호화된 비밀번호로 회원가입
            if (result.hasErrors()) {
                return "guest/register"; // 에러가 있으면 다시 폼 페이지로
            }
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다! 로그인하세요.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            result.rejectValue("guest_idid", "error.guest", e.getMessage());
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

    @GetMapping("/myPage")
    public String showmyPage(Model model) {
        // ✅ 현재 로그인된 사용자 가져오기
        String username = getCurrentUsername();
        if (username == null) {
            return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로 이동
        }

        // ✅ 사용자 정보에서 커플 코드 가져오기
        Guest guest = guestRepository.findByIdid(username).orElse(null);
        Couple couple = coupleRepository.findByGuest1Id(Long.valueOf(guest.getIdid()));
        if (guest == null) {
            return "redirect:/login";
        }

        String myCoupleCode = guest.getCoupleCode(); // 현재 로그인된 사용자의 커플 코드
//        String partnerCoupleCode = guest.getPendingCoupleCode(); // 상대방 커플 코드
        String partnerCoupleCode = couple.getGuest2().getCoupleCode();
        log.info("asdfasdf");
        System.out.println("myCoupleCode = " + myCoupleCode);
        System.out.println("partnerCoupleCode = " + partnerCoupleCode);

        String guest1Name = coupleService.getGuest1Name(myCoupleCode);
        String guest2Name = coupleService.getGuest2Name(partnerCoupleCode);

        System.out.println("guest1Name = " + guest1Name);
        System.out.println("guest2Name = " + guest2Name);
//        if (coupleService.getGuest1Name(myCoupleCode) != null)
//            guest1Name = coupleService.getGuest1Name(myCoupleCode);
//        if (coupleService.getGuest2Name(partnerCoupleCode) != null)
//            guest2Name = coupleService.getGuest2Name(partnerCoupleCode);

        model.addAttribute("guest1Name", guest1Name);
        model.addAttribute("guest2Name", guest2Name);
        model.addAttribute("coupleCode", myCoupleCode);

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


    @GetMapping("/mypage/reservationStatus")
    public String reservationStatus(Model model) {
        return "mypage/reservationStatus";
    }

    @GetMapping("/mypage/reservationEdit")
    public String reservationEdit(Model model) {
        return "mypage/reservationEdit";
    }

    @GetMapping("/mypage/reservationCancel")
    public String reservationCancel(Model model) {
        return "mypage/reservationCancel";
    }

    //email 변경
    @Transactional
    @PostMapping("/mypage/guest/email")
    public String updateEmail(@RequestParam("guestId") Long guestId,
                              @RequestParam("email") String email,
                              RedirectAttributes redirectAttributes) {
        Optional<Guest> guestOptional = guestRepository.findById(guestId);

        if (guestOptional.isPresent()) {
            Guest guest = guestOptional.get();
            guest.setEmail(email);
            guestRepository.save(guest);
            redirectAttributes.addFlashAttribute("emailMessage", "이메일이 성공적으로 변경되었습니다!");
        } else {
            redirectAttributes.addFlashAttribute("emailError", "사용자를 찾을 수 없습니다.");
        }
        return "redirect:/mypage/profile";
    }

    //전화번호 변경
    @Transactional
    @PostMapping("/mypage/guest/updatePhone")
    public String updatePhone(@RequestParam("guestId") Long guestId,
                              @RequestParam("phone") String phone,
                              RedirectAttributes redirectAttributes) {
        Optional<Guest> guestOptional = guestRepository.findById(guestId);

        if (guestOptional.isPresent()) {
            Guest guest = guestOptional.get();
            guest.setPhone(phone);
            guestRepository.save(guest);

            redirectAttributes.addFlashAttribute("phoneMessage", "전화번호가 성공적으로 변경되었습니다!");
        } else {
            redirectAttributes.addFlashAttribute("phoneError", "사용자를 찾을 수 없습니다.");
        }

        return "redirect:/mypage/profile";
    }

    //비밀번호 변경
    @PostMapping("/mypage/guest/updatePassword")
    public String updatePassword(@RequestParam("guestId") Long guestId,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 RedirectAttributes redirectAttributes) {
        Optional<Guest> guestOptional = guestRepository.findById(guestId);

        if (guestOptional.isPresent()) {
            Guest guest = guestOptional.get();

            // 현재 비밀번호 확인 (Spring Security 사용 시)
            if (!passwordEncoder.matches(currentPassword, guest.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "현재 비밀번호가 일치하지 않습니다.");
                return "redirect:/mypage/profile";
            }

            // 새 비밀번호 암호화 후 저장
            guest.setPassword(passwordEncoder.encode(newPassword));
            guestRepository.save(guest);

            // 성공 메시지 설정
            redirectAttributes.addFlashAttribute("passwordMessage", "비밀번호가 성공적으로 변경되었습니다!");
        } else {
            redirectAttributes.addFlashAttribute("passwordError", "사용자를 찾을 수 없습니다.");
        }
        return "redirect:/mypage/profile";
    }
}

