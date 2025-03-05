package com.example.mingle.service;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GuestService implements UserDetailsService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public GuestService(GuestRepository guestRepository, PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long join(Guest guest) {
        validateDuplicationMember(guest);
        guest.setPassword(passwordEncoder.encode(guest.getPassword())); // ✅ 비밀번호 암호화
        guestRepository.save(guest);
        return guest.getId();
    }

    private void validateDuplicationMember(Guest guest) {
        guestRepository.findByName(guest.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public boolean validateLogin(String idid, String password) {
        Optional<Guest> optionalGuest = guestRepository.findByIdid(idid);
        if (optionalGuest.isPresent() && optionalGuest.get().getPassword().equals(password)) {
            System.out.println("LOGIN SUCCESS");
            return true;
        }
        return false;
    }

    public List<Guest> findUser() {
        return guestRepository.findAll();
    }

//    public Optional<Guest> findOne(Long userId) {
//        return guestRepository.findById(userId);
//    }
// ✅ Spring Security 로그인 처리
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Guest guest = guestRepository.findByIdid(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return User.builder()
            .username(guest.getIdid())
            .password(guest.getPassword()) // Spring Security가 자동으로 비밀번호 검증
            .roles("USER")
            .build();
    }
}
