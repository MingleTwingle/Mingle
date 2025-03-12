package com.example.mingle.service;

import com.example.mingle.domain.Couple;
import com.example.mingle.domain.Guest;
import com.example.mingle.repository.CoupleRepository;
import com.example.mingle.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoupleService {

    private final CoupleRepository coupleRepository;
    private final GuestRepository guestRepository;

    public String requestCoupleMatching(String myCoupleCode, String partnerCoupleCode) {
        Guest myGuest = guestRepository.findByCoupleCode(myCoupleCode).orElse(null);
        Guest partnerGuest = guestRepository.findByCoupleCode(partnerCoupleCode).orElse(null);

        if (myGuest == null || partnerGuest == null) {
            return "fail";
        }

        // 서로의 커플 코드가 맞교환된 경우 → 매칭 성립
        if (partnerCoupleCode.equals(myGuest.getPendingCoupleCode()) ||
                myCoupleCode.equals(partnerGuest.getPendingCoupleCode())) {

            // 매칭 성립 → 상태 설정
            myGuest.setMatched(true);
            partnerGuest.setMatched(true);

            // Couple 엔티티 저장
            Couple couple = new Couple();
            couple.setGuest1(myGuest);
            couple.setGuest2(partnerGuest);
            couple.setMatched(true);
            coupleRepository.save(couple);

            // 상태 저장
            guestRepository.save(myGuest);
            guestRepository.save(partnerGuest);

            return "success";
        }

        // 상대방의 커플 코드 대기 상태 설정
        myGuest.setPendingCoupleCode(partnerCoupleCode);
        guestRepository.save(myGuest);

        return "pending";
    }

    // ✅ guest1 이름 가져오기
    public String getGuest1Name(String myCoupleCode) {
        if (myCoupleCode == null) {
            return null;
        }
        Guest guest = guestRepository.findByCoupleCode(myCoupleCode).orElse(null);
        if (guest != null) {
            Couple couple = coupleRepository.findByGuest1Id(guest.getId());
            return couple != null ? couple.getGuest1().getName() : null;
        }
        return null;
    }

    // ✅ guest2 이름 가져오기
    public String getGuest2Name(String partnerCoupleCode) {
        if (partnerCoupleCode == null) {
            return null;
        }
        Guest guest = guestRepository.findByCoupleCode(partnerCoupleCode).orElse(null);
        if (guest != null) {
            Couple couple = coupleRepository.findByGuest2Id(guest.getId());
            return couple != null ? couple.getGuest2().getName() : null;
        }
        return null;
    }
}
