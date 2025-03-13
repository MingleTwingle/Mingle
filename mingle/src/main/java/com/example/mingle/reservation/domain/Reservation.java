package com.example.mingle.reservation.domain;

import com.example.mingle.accommodation.domain.AccommodationRoom;
import com.example.mingle.domain.Guest;
import com.example.mingle.domain.Host;
import com.example.mingle.restaurant.domain.Restaurant;
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

    private String newTime;
    private String restaurantName;
    private String accommodationRoomName;
    private String dated;

    // 레스토랑과의 관계 매핑 (restaurant_id를 직접 참조)
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    // 숙소 객실과의 관계 매핑 (accommodation_room_Id를 직접 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_room_Id", referencedColumnName = "accommodation_room_Id")
    private AccommodationRoom accommodationRoom;  // 필드 이름은 'accommodationRoom'

    // guest 외래 키 연결
    @ManyToOne
    @JoinColumn(name = "guest_key")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "host_key")
    private Host host;
}
