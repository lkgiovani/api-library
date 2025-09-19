package com.example.demo.domain;



import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Book {
    private String id;
    private String isbn;
    private String title;
    private String author;
    private int stock;
    private long priceInCents;

}
