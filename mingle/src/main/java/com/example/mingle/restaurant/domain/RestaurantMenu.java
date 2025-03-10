package com.example.mingle.restaurant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_restaurant_menu_info")
public class RestaurantMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_menu_id")
    private Long id;

    @Column(name = "restaurant_menu_menu")
    private String menu;

    @Column(name = "restaurant_menu_category")
    private String category;

    @Column(name = "restaurant_menu_cost")
    private int cost;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

}
