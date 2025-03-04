package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "tbl_accommodation_photo")
public class AccommodationPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_photo_id")
    private int id;

    @Lob  // 바이너리 데이터를 저장할 때 사용
    @Column(name = "accommodation_photo")
    private byte[] photo;  // 이미지는 byte[] 형식으로 저장

    @Column(name = "accommodation_room_id")
    private int roomId;
}
