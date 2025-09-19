package com.example.demo.gateways.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, length = 17)
    private String isbn;

    private String title;

    private String author;

    private int stock;

    private long priceInCents;


}
