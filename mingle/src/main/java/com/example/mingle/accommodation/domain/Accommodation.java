package com.example.mingle.accommodation.domain;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_accommodation_info")
@Getter
@Setter
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_id")
    private Integer id;

    @Column(name = "accommodation_name", length = 20)
    private String name;

    @Column(name = "accommodation_location", length = 20)
    private String location;

    @Column(name = "accommodation_parking")
    private boolean parkingAvailable;

    @Column(name = "accommodation_morning")
    private boolean morningAvailable;

    @Column(name = "accommodation_checkin_time")
    private LocalTime checkInTime;

    @Column(name = "accommodation_checkout_time")
    private LocalTime checkOutTime;

    // 1:N 관계 설정
    @OneToMany(mappedBy = "accommodation")
    private List<AccommodationRoom> rooms;

    @OneToMany(mappedBy = "accommodation")
    private List<AccommodationOutterPhoto> outterPhotos;

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

}
