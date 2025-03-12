package com.example.mingle.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_couple")
public class Couple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couple_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "guest_1_id", referencedColumnName = "guest_key")
    private Guest guest1;

    @OneToOne
    @JoinColumn(name = "guest_2_id", referencedColumnName = "guest_key")
    private Guest guest2;

    @Column(name = "is_matched", nullable = false)
    private boolean isMatched = false;

}
