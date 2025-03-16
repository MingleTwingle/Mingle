package com.example.mingle.accommodation.domain;

import java.util.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Entity
@Table(name = "tbl_acco_outter_photo")
@Slf4j
public class AccommodationOutterPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acco_out_photo_id")
    private Long id;

    @Lob
    @Column(name = "accommodation_outter_photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    // getImage()는 이미지 데이터를 Base64로 인코딩한 URL 형식으로 반환하는 메서드
    public String getImage() {
        log.info("im in getImage ");

        if (photo == null) {
            log.info("사진 데이터가 NULL입니다");
            return null;
        }

        if (photo.length == 0) {
            log.info("사진 데이터가 비어 있습니다");
            return null;
        }

        String base64Image = Base64.getEncoder().encodeToString(photo);
        log.info("Base64 길이: " + base64Image.length());
        return "data:image/jpeg;base64," + base64Image;

    }

}
