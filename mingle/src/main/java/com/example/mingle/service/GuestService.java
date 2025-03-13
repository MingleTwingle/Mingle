package com.example.mingle.service;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.UUID;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public GuestService(GuestRepository guestRepository, HostRepository hostRepository, PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long join(Guest guest, BindingResult result) {
        validateDuplicationMember(guest, result);
        guest.setPassword(passwordEncoder.encode(guest.getPassword())); // 비밀번호 암호화

        //  커플 코드 자동 생성
        if (guest.getCoupleCode() == null || guest.getCoupleCode().isEmpty()) {
            guest.setCoupleCode(generateUniqueCoupleCode());
        }

        guestRepository.save(guest);
        return guest.getId();
    }

    private void validateDuplicationMember(Guest guest, BindingResult result) {
        guestRepository.findByIdid(guest.getIdid())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
        hostRepository.findByIdid(guest.getIdid())
                .ifPresent(h -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    //  랜덤 커플 코드 생성
    private String generateUniqueCoupleCode() {
        String newCode;
        do {
            newCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // 8자리 생성
        } while (guestRepository.findByCoupleCode(newCode).isPresent()); // 중복 검사
        return newCode;
    }

    public List<Guest> findUser() {
        return guestRepository.findAll();
    }



    public Guest findByIdid(String idid) {
        return guestRepository.findByIdid(idid).orElse(null);
    }

    @Transactional
    public void deleteGuestById(Long id) {
        guestRepository.deleteById(id);
    }

}
