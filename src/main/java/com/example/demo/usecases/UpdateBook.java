package com.example.demo.usecases;

import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBook {

    private final BookRepository repository;

    public Book handle(String id, BookPutRequestDTO dto) {
        Book existingBook = repository.findById(id).
                orElseThrow(() -> new CustomHttpException(
                        "Book not found",
                        HttpStatus.NOT_FOUND.value()
                ));

        if (dto.title() != null) {
            existingBook.setTitle(dto.title());
        }

        if (dto.author() != null) {
            existingBook.setAuthor(dto.author());
        }

        if (dto.priceInCents() != null) {
            existingBook.setPriceInCents(dto.priceInCents());
        }

        if (dto.stock() != null) {
            existingBook.setStock(dto.stock());
        }

        return repository.save(existingBook);
    }
}