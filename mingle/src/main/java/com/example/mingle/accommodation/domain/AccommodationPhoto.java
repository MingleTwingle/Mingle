package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_accommodation_photo")
public class AccommodationPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_photo_id")
    private Long id;

    @Lob
    @Column(name = "accommodation_photo")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "accommodation_room_id")  // room ID와 연결
    private AccommodationRoom accommodationRoom;
}
