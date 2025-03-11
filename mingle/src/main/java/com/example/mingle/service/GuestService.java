package com.example.mingle.service;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public GuestService(GuestRepository guestRepository, PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long join(Guest guest) {
        validateDuplicationMember(guest);
        guest.setPassword(passwordEncoder.encode(guest.getPassword())); // 비밀번호 암호화
        guestRepository.save(guest);
        return guest.getId();
    }

    private void validateDuplicationMember(Guest guest) {
        guestRepository.findByName(guest.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Guest> findUser() {
        return guestRepository.findAll();
    }

    public Guest findByEmail(String email) {
        return (Guest) guestRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일로 사용자를 찾을 수 없습니다."));
    }

    public Guest findByIdid(String idid) {
        return guestRepository.findByIdid(idid).orElse(null);
    }
}
