package com.example.mingle.restaurant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_restaurantmenu_info")
public class RestaurantMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurantmenu_id")
    private Long id;

    @Column(name = "restaurantmenu_menu")
    private String menu;

    @Column(name = "restaurantmenu_category")
    private String category;

    @Column(name = "restaurantmenu_cost")
    private int cost;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

}
