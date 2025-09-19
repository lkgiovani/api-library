package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.gateways.BookGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBooks {

    private final BookGateway bookGateway;

    public List<Book> execute() {
        return bookGateway.listAll();
    }
}

