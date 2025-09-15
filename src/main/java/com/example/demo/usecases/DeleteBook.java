package com.example.demo.usecases;

import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBook {
    private final BookRepository repository;

    public void handle(String id) {
       repository.findById(id)
               .orElseThrow(() -> new CustomHttpException(
                       "Book not found",
                       HttpStatus.NOT_FOUND.value()
               ));

        repository.deleteById(id);
    }
}
