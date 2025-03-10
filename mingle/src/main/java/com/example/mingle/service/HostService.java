package com.example.mingle.service;

import com.example.mingle.domain.Host;
import com.example.mingle.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Host> findHost() { return hostRepository.findAll(); }

}
