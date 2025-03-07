package com.example.mingle.repository;

import com.example.mingle.domain.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    Host save(Host host);
    Optional<Host> findByIdid(String idid);
    Optional<Host> findByName(String name);
    List<Host> findAll();
}
