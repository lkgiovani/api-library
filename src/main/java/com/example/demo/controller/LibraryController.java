package com.example.demo.controller;

import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.controller.dtos.BookResponseDTO;
import com.example.demo.controller.mapper.BookMapper;
import com.example.demo.usecases.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/books")
@RestController
public class LibraryController {

    private final CreateBook createBook;
    private final GetBooks getBooks;
    private final GetBook getBook;
    private final UpdateBook updateBook;
    private final DeleteBook deleteBook;
    private final BookMapper bookMapper;

    @ResponseStatus(CREATED)
    @PostMapping
    public BookResponseDTO createBook(@RequestBody @Valid final BookPostRequestDTO bookPostRequestDTO) {
        return bookMapper.toDTO(createBook.execute(bookMapper.toDomain(bookPostRequestDTO)));
    }

    @ResponseStatus(OK)
    @GetMapping
    public List<BookResponseDTO> getBooks() {
        return getBooks.execute().stream().map(bookMapper::toDTO).toList();
    }

    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public BookResponseDTO getBook(@PathVariable("id") String id) {
        return bookMapper.toDTO(getBook.execute(id));
    }

    @ResponseStatus(OK)
    @PutMapping("/{id}")
    public BookResponseDTO updateBook(@PathVariable("id") String id, @RequestBody final BookPutRequestDTO dto) {
        return bookMapper.toDTO(updateBook.execute(id, dto));
    }

    @ResponseStatus(OK)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        deleteBook.execute(id);
    }
}
