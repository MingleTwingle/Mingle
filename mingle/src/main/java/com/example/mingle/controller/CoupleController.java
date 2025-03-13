package com.example.mingle.controller;

import com.example.mingle.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CoupleController {

    private final CoupleService coupleService;

    @GetMapping("/couple/register")
    public String createCoupleForm(Model model) {
        model.addAttribute("coupleForm", new CoupleForm());
        return "couple/register";
    }

    @PostMapping("/couple/register")
    public String registerCouple(
            @RequestParam String myCoupleCode,
            @RequestParam String partnerCoupleCode,
            RedirectAttributes redirectAttributes) {

        String result = coupleService.requestCoupleMatching(myCoupleCode, partnerCoupleCode);

        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "커플 등록이 성공하였습니다!");
            return "redirect:/mypage";
        } else if ("pending".equals(result)) {
            redirectAttributes.addFlashAttribute("infoMessage", "상대방이 커플 코드를 입력하면 매칭이 완료됩니다.");
            return "redirect:/couple/register";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "커플 등록에 실패하였습니다. 다시 시도해 주세요.");
            return "redirect:/couple/register";
        }
    }
}
