package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_accommodation_outter_photo")
public class AccommodationOutterPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accoOutPhoto_id")
    private Long id;

    @Lob
    @Column(name = "accommodation_outter_photo")
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;
}

