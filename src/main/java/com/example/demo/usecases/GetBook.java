package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.gateways.BookGateway;
import com.example.demo.gateways.entity.BookEntity;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBook {

    private final BookGateway BookGateway;

    public Book execute(String id){
        return BookGateway.getBookById(id);
    }
}
