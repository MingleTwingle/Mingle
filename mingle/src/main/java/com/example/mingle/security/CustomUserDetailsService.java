package com.example.mingle.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String idid) throws UsernameNotFoundException {
        log.info("🔍 로그인 시도: " + idid); // 디버깅 로그

        // Guest 검색
        Guest guest = guestRepository.findByIdid(idid).orElse(null);
        if (guest != null) {
            log.info("✅ 로그인 성공 (Guest): " + guest.getIdid());
            return new CustomUserDetails(guest.getIdid(), guest.getPassword(), "ROLE_USER");

        }

        // Host 검색
        Host host = hostRepository.findByIdid(idid).orElse(null);
        if (host != null) {
            log.info("✅ 로그인 성공 (Host): " + host.getIdid());
            return new CustomUserDetails(host.getIdid(), host.getPassword(), "ROLE_HOST");

        }

        log.info("❌ 로그인 실패: 아이디 없음");
        throw new UsernameNotFoundException("User not found with idid: " + idid);
    }

    // 비밀번호 검증
    public boolean validateLogin(String idid, String password) {
        Optional<Guest> optionalGuest = guestRepository.findByIdid(idid);
        if (optionalGuest.isPresent() && passwordEncoder.matches(password, optionalGuest.get().getPassword())) {
            log.info("LOGIN SUCCESS (Guest)");
            return true;
        }

        Optional<Host> optionalHost = hostRepository.findByIdid(idid);
        if (optionalHost.isPresent() && passwordEncoder.matches(password, optionalHost.get().getPassword())) {
            log.info("LOGIN SUCCESS (Host)");
            return true;
        }

        return false;
    }

    // 그 외의 메서드들은 필요에 따라 추가할 수 있습니다.
}
