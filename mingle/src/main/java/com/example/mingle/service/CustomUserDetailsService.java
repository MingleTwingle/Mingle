package com.example.mingle.service;

import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;

    @Autowired
    public CustomUserDetailsService(GuestRepository guestRepository, HostRepository hostRepository) {
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String idid) throws UsernameNotFoundException {
        System.out.println("🔍 로그인 시도: " + idid);

        // Guest 먼저 검색
        Guest guest = guestRepository.findByIdid(idid).orElse(null);
        if (guest != null) {
            System.out.println("✅ 로그인 성공 (Guest): " + guest.getIdid());
            return User.builder()
                    .username(guest.getIdid())
                    .password(guest.getPassword())
                    .roles("USER")
                    .build();
        }

        // Host 검색
        Host host = hostRepository.findByIdid(idid).orElse(null);
        if (host != null) {
            System.out.println("✅ 로그인 성공 (Host): " + host.getIdid());
            return User.builder()
                    .username(host.getIdid())
                    .password(host.getPassword())
                    .roles("HOST")
                    .build();
        }

        System.out.println("❌ 로그인 실패: 아이디 없음");
        throw new UsernameNotFoundException("User not found with idid: " + idid);
    }
}
