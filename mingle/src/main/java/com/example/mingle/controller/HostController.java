package com.example.mingle.controller;

import com.example.mingle.domain.Host;
import com.example.mingle.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HostController {
    private final HostService hostService;

    @Autowired
    public HostController(HostService hostService) {
        this.hostService = hostService;
    }

    // 호스트 등록 페이지
    @GetMapping("/host/register")
    public String showHostRegisterForm() { return "host/register"; }

    @PostMapping("/host/register")
    public String create(@Validated HostForm form, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "host/register";
        }

        Host host = new Host();
        host.setName(form.getHost_name());
        host.setIdid(form.getHost_idid());
        host.setNickname(form.getHost_nickname());
        host.setPassword(form.getHost_password());
        host.setEmail(form.getHost_email());
        host.setGender(form.getHost_gender());
        host.setPhone(form.getHost_phone_number());
        host.setType(form.getHost_type());

        try {
            hostService.join(host);
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 완료되었습니다! 로그인하세요.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            result.rejectValue("idid", "error.host", "이미 존재하는 아이디입니다.");
            return "host/register";
        }
    }
}
