package com.example.mingle.restaurant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_restaurant_photo")
public class RestaurantPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_photo_id")
    private Long id;

    @Lob  // BLOB 타입 지정
    @Column(name = "restaurant_photo", columnDefinition = "LONGBLOB")
    private byte[] photoData; // 이미지 데이터를 바이트 배열로 저장

    @ManyToOne
    @JoinColumn(name = "restaurant_menu_id")
    private RestaurantMenu restaurantMenu;
}
