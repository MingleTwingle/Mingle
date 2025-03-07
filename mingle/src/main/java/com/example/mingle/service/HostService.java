package com.example.mingle.service;

import com.example.mingle.domain.Host;
import com.example.mingle.repository.HostRepository;
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
public class HostService {

    private final HostRepository hostRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HostService(HostRepository hostRepository, PasswordEncoder passwordEncoder) {
        this.hostRepository = hostRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long join(Host host) {
        validateDuplicationMember(host);
        host.setPassword(passwordEncoder.encode(host.getPassword()));
        hostRepository.save(host);
        return host.getId();
    }

    private void validateDuplicationMember(Host host) {
        hostRepository.findByName(host.getName())
                .ifPresent(m ->{
                        throw new IllegalStateException("이미 존재하는 호스트입니다.");
                });
    }

    public boolean validateLogin(String idid, String password) {
        Optional<Host> optionalHost = hostRepository.findByIdid(idid);
        if (optionalHost.isPresent() && optionalHost.get().getPassword().equals(password)) {
            System.out.println("LOGIN SUCCESS");
            return true;
        }
        return false;
    }

    public List<Host> findHost() { return hostRepository.findAll(); }

//    @Override
//    public UserDetails loadUserByUsername(String idid) throws UsernameNotFoundException {
//        System.out.println("🔍 로그인 시도: " + idid); // 디버깅 로그'
//
//        Host host = hostRepository.findByIdid(idid)
//                .orElseThrow(() -> {
//                    System.out.println("❌ 로그인 실패: 아이디 없음");
//                    return new UsernameNotFoundException("User not found");
//                });
//        System.out.println("✅ 로그인 성공: " + host.getIdid());
//
//        return User.builder()
//                .username(host.getIdid())
//                .password(host.getPassword())
//                .roles("USER")
//                .build();
//    }
}
