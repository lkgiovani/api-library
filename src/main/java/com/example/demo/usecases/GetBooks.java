package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBooks {

    private final BookRepository repository;

    public List<Book> handle() {
        return repository.findAll();
    }
}

