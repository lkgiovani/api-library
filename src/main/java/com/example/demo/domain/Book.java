package com.example.demo.domain;


import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, length = 17)
    private String isbn;

    private String title;

    private String author;

    private int stock;

    private long priceInCents;

    public static Book create(String title, String author, String isbn, int stock, long priceInCents) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setStock(stock);
        book.setPriceInCents(priceInCents);
        return book;
    }

    public static Book createFromPostDTO(BookPostRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setIsbn(dto.isbn());
        book.setStock(dto.stock());
        book.setPriceInCents(dto.priceInCents());
        return book;
    }

    public static Book createFromPutDTO(BookPostRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setIsbn(dto.isbn());
        book.setStock(dto.stock());
        book.setPriceInCents(dto.priceInCents());
        return book;
    }
}
