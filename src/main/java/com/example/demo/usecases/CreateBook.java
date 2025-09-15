package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateBook {

    private final BookRepository repository;

    public Book handle(Book cmd) {
        repository.findById(cmd.getIsbn())
                .ifPresent(existingBook -> {
                    throw new CustomHttpException(
                            "Book with ISBN already exists: " + cmd.getIsbn(), 
                            HttpStatus.CONFLICT.value()
                    );
                });

        return repository.save(cmd);
    }
}
