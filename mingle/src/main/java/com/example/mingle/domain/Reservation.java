package com.example.mingle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_reservation", uniqueConstraints = {@UniqueConstraint(columnNames = {"reservation_id"})
})
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;
    @Column(name = "reservation_date")
    private String date;
    @Column(name = "reservation_people")
    private String people;
    @Column(name = "reservation_cancel")
    private String cancel;
    @Column(name = "restaurant_id")
    private String restaurant_id;
    @Column(name = "accommodationRoom_Id")
    private String accommodationRoom_Id;
    @Column(name = "guest_key")
    private String key;
    private String newTime;

    public void setTime(String newTime) {
        this.newTime = newTime;
    }
}
