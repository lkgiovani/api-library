package com.example.demo.usecases;

import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.gateways.BookGateway;
import com.example.demo.gateways.mapper.BookGatewayMapper;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBook {

    private final BookGateway bookGateway;

    public void execute(String id) {
        bookGateway.deleteBookById(id);
    }
}
