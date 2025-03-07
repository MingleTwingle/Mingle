package com.example.mingle.restaurant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@Entity
@Table(name = "tbl_restaurant_outter_photo")

public class RestaurantOutterPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rest_out_photo_id")
    private Long id;

    @Lob
    @Column(name = "restaurant_outter_photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // getImage()는 이미지 데이터를 Base64로 인코딩한 URL 형식으로 반환하는 메서드
    public String getImage() {
        System.out.println("im in getImage!?!?!?!?");

        if (photo == null) {
            System.out.println("사진 데이터가 NULL입니다");
            return null;
        }

        if (photo.length == 0) {
            System.out.println("사진 데이터가 비어 있습니다");
            return null;

        }

        String base64Image = Base64.getEncoder().encodeToString(photo);
        System.out.println("Base64 길이: " + base64Image.length());
        return "data:image/jpeg;base64," + base64Image;

    }
}
