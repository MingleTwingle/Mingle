package com.example.mingle.service;

import com.example.mingle.domain.Guest;
import com.example.mingle.repository.GuestRepository;

import java.util.List;

public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Long join(Guest guest) {
        validateDuplicationMember(guest);
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

//    public Optional<Guest> findOne(Long userId) {
//        return guestRepository.findById(userId);
//    }
}
