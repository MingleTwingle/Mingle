package com.example.mingle.security;

import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
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
            return new CustomUserDetails(guest.getIdid(), guest.getPassword(), "ROLE_USER");
        }

        // Host 검색
        Host host = hostRepository.findByIdid(idid).orElse(null);
        if (host != null) {
            System.out.println("✅ 로그인 성공 (Host): " + host.getIdid());
            return new CustomUserDetails(host.getIdid(), host.getPassword(), "ROLE_HOST");
        }

        System.out.println("❌ 로그인 실패: 아이디 없음");
        throw new UsernameNotFoundException("User not found with idid: " + idid);
    }
}
