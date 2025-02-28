package com.example.mingle.accommodation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_accommodation_info")  // 테이블 이름을 소문자로 변경
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
    private LocalDateTime checkInTime;

    @Column(name = "accommodation_checkout_time")
    private LocalDateTime checkOutTime;

    @Override
    public String toString() {
        return "Accommodation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

}
