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
    @Column(name = "restaurantphoto_id")
    private Long id;

    @Lob
    @Column(name = "restaurant_photo")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "restaurantmenu_id")
    private RestaurantMenu restaurantMenu;
}
