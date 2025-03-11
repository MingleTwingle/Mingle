package com.example.mingle.profile.service;

import com.example.mingle.domain.Host;
import com.example.mingle.repository.HostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private HostRepository hostRepository;

    public boolean updateHostProfile(String field, String newValue, Long hostId) {
        if (hostId == null) {
            return false; // 로그인하지 않은 경우
        }

        Optional<Host> hostOptional = hostRepository.findById(hostId);

        if (hostOptional.isPresent()) {
            Host host = hostOptional.get();

            switch (field) {
                case "name":
                    host.setName(newValue);
                    break;
                case "nickname":
                    host.setNickname(newValue);
                    break;
                case "email":
                    host.setEmail(newValue);
                    break;
                case "phone":
                    host.setPhone(newValue);
                    break;
                case "gender":
                    if (!newValue.equals("남성") && !newValue.equals("여성")) {
                        return false; // 성별이 올바르지 않은 경우
                    }
                    host.setGender(newValue);
                    break;
                case "type":
                    host.setType(newValue);
                    break;
                default:
                    return false;
            }

            hostRepository.save(host);
            return true;
        }
        return false;
    }
}
