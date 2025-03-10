package com.example.mingle.domain;

import com.example.mingle.reservation.domain.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "tbl_guest", uniqueConstraints = {@UniqueConstraint(columnNames = {"guest_idid"})})
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_key")
    private Long id;

    @Column(name = "guest_name")
    private String name;

    @Column(name = "guest_idid")
    private String idid;

    @Column(name = "guest_nickname")
    private String nickname;

    @Column(name = "guest_password")
    private String password;

    @Column(name = "guest_email")
    private String email;

    @Column(name = "guest_couple_code")
    private String coupleCode;

    @Column(name = "guest_phone_number")
    private String phone;

    @Column(name = "guest_gender")
    private String gender;

    // reservations 필드에서 mappedBy 수정: "guest"로 수정
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
