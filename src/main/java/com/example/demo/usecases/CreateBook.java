package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.gateways.BookGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBook {

    private final BookGateway bookGateway;

    public Book execute(Book book) {
        return bookGateway.createBook(book);
    }
}
