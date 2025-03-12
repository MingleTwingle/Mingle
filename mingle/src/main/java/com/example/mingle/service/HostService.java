package com.example.mingle.service;

import com.example.mingle.domain.Host;
import com.example.mingle.repository.GuestRepository;
import com.example.mingle.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class HostService {

    private final HostRepository hostRepository;
    private final GuestRepository  guestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public HostService(HostRepository hostRepository, GuestRepository guestRepository, PasswordEncoder passwordEncoder) {
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long join(Host host, BindingResult result) {
        validateDuplicationMember(host, result);
        host.setPassword(passwordEncoder.encode(host.getPassword())); // 비밀번호 암호화
        hostRepository.save(host);
        return host.getId();
    }

    private void validateDuplicationMember(Host host, BindingResult result) {
        hostRepository.findByIdid(host.getIdid())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
        guestRepository.findByIdid(host.getIdid())
                .ifPresent(g -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Host> findUser() {
        return hostRepository.findAll();
    }

    public Host findByIdid(String idid) {
        return hostRepository.findByIdid(idid).orElse(null);
    }

    @Transactional
    public void deleteHostById(Long id) {
        hostRepository.deleteById(id);
    }

}
