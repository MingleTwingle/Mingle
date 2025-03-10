package com.example.mingle.reservation.domain;

import com.example.mingle.domain.Guest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_reservation", uniqueConstraints = {@UniqueConstraint(columnNames = {"reservation_id"})})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "reservation_date")
    private String date;

    @Column(name = "reservation_people")
    private String people;

    @Column(name = "reservation_cancel")
    private String cancel;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "accommodationRoom_Id")
    private Long accommodationRoomId;

    // guest_key 외래 키 연결
    @ManyToOne
    @JoinColumn(name = "guest_key")
    private Guest guest;  // guest라는 필드명으로 변경

    private String newTime;

    public void setTime(String newTime) {
        this.newTime = newTime;
    }

    public void setRestaurantId(Long restaurantId) {
        if (restaurantId == null || restaurantId.equals("")) {
            this.restaurantId = null;
        } else {
            this.restaurantId = restaurantId;
        }
    }



    public void setAccommodationRoomId(Long accommodationRoomId) {
        if (accommodationRoomId == null || accommodationRoomId.equals("")) {
            this.accommodationRoomId = null;
        } else {
            this.accommodationRoomId = accommodationRoomId;
        }
    }

}
