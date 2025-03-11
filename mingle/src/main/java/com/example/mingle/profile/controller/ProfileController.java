package com.example.mingle.profile.controller;

import com.example.mingle.profile.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hostProfile")
public class ProfileController {

    @GetMapping("/hostProfile")
    public String shohostProfileForm() {
        return "mypage/hostProfile";
    }

    @Autowired
    private ProfileService profileService;

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, String> request, HttpSession session) {
        String field = request.get("field");
        String newValue = request.get("value");
        Long hostId = (Long) session.getAttribute("hostId"); // ✅ 세션에서 hostId 가져오기

        boolean success = profileService.updateHostProfile(field, newValue, hostId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ? "변경 성공" : "변경 실패");

        return ResponseEntity.ok(response);
    }
}
