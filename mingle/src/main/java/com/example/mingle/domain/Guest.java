package com.example.mingle.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_guest", uniqueConstraints = { @UniqueConstraint(columnNames = { "guest_idid" }) })
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_key")
    private Long id;
    @Column(name = "guest_name")
    private String name;
    @Column(name = "guest_idid", unique = true)
    private String idid;
    @Column(name = "guest_nickname")
    private String nickname;
    @Column(name = "guest_password")
    private String password;
    @Column(name = "guest_email")
    private String email;
    @Column(name = "guest_couple_code")
    private String coupleCode;

    @Column(name = "pending_couple_code")
    private String pendingCoupleCode;

    @Column(name = "is_matched", nullable = false)
    private boolean isMatched = false;

    @PrePersist // 회원가입(INSERT) 전에 자동 실행되는 메서드
    public void generateCoupleCode() {
        if (this.coupleCode == null || this.coupleCode.isEmpty()) {
            this.coupleCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // 8자리 코드 랜덤 생성
        }
    }

    @Column(name = "guest_phone_number")
    private String phone;
    @Column(name = "guest_gender")
    private String gender;
}

