package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@Entity
@Table(name = "tbl_acco_outter_photo")
public class AccommodationOutterPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acco_out_photo_id")
    private Long id;

    @Lob
    @Column(name = "accommodation_outter_photo")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    // getImage()는 이미지 데이터를 Base64로 인코딩한 URL 형식으로 반환하는 메서드
    public String getImage() {
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo);
    }
}

