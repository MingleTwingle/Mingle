package com.example.mingle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_host", uniqueConstraints = {@UniqueConstraint(columnNames = {"host_idid"})})
public class Host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_key")
    private Long id;

    @Column(name = "host_name")
    private String name;

    @Column(name = "host_idid", unique = true) // 유니크 제약 조건 추가
    private String idid;

    @Column(name = "host_nickname")
    private String nickname;

    @Column(name = "host_password")
    private String password;

    @Column(name = "host_email")
    private String email;

    @Column(name = "host_phone_number")
    private String phone;

    @Column(name = "host_gender")
    private String gender;

    @Column(name = "host_type")
    private String type;

    // ✅ 잘못된 @OneToMany 삭제
}
