package com.example.demo.controller;

import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.domain.Book;
import com.example.demo.usecases.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/books")
@RestController
public class LibraryController {

    private final CreateBook createBook;
    private final GetBooks getBooks;
    private final GetBook getBook;
    private final UpdateBook updateBook;
    private final DeleteBook deleteBook;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book createBook(@RequestBody @Valid final BookPostRequestDTO dto) {
        Book book = Book.createFromPostDTO(dto);
        return createBook.handle(book);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Book> getBooks() {
        return getBooks.handle();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Book getBook(@PathVariable("id") String id) {
        return getBook.handle(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable("id") String id, @RequestBody final BookPutRequestDTO dto) {
        return updateBook.handle(id, dto);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        deleteBook.handle(id);
    }
}
