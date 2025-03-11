package com.example.mingle.controller;

import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/delate_account")
public class AccountController {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private HostRepository hostRepository;

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteAccount(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Long guestId = (Long) session.getAttribute("guestId");
        Long hostId = (Long) session.getAttribute("hostId");

        if (guestId != null) {
            guestRepository.deleteById(guestId);
            session.invalidate();
            response.put("success", true);
            response.put("message", "게스트 계정이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        }

        if (hostId != null) {
            hostRepository.deleteById(hostId);
            session.invalidate();
            response.put("success", true);
            response.put("message", "호스트 계정이 삭제되었습니다.");
            return ResponseEntity.ok(response);
        }

        response.put("success", false);
        response.put("message", "회원 정보를 찾을 수 없습니다.");
        return ResponseEntity.badRequest().body(response);
    }
}
