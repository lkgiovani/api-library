package com.example.demo.usecases;

import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.domain.Book;
import com.example.demo.gateways.BookGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBook {

    private final BookGateway bookGateway;

    public Book execute(String id, BookPutRequestDTO dto) {
        return bookGateway.updateBook(id, dto);
    }

}