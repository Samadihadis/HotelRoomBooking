package com.samadihadis.hotelroombooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String description;

    @Max(value = 5)
    private Integer rate;

    @Max(value = 5)
    private Integer starRating;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

}
