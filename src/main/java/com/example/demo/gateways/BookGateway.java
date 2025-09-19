package com.example.demo.gateways;

import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.controller.mapper.BookMapper;
import com.example.demo.domain.Book;
import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.gateways.mapper.BookGatewayMapper;
import com.example.demo.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookGateway {

    private final BookRepository bookRepository;
    private final BookGatewayMapper bookGatewayMapper;
    private final BookMapper bookMapper;

    public Book createBook(Book book) {
        return bookGatewayMapper.toBookDomain(bookRepository.save(bookGatewayMapper.toBookEntity(book)));
    }

    public List<Book> listAll() {
        return bookRepository.findAll().stream().map(bookGatewayMapper::toBookDomain).toList();
    }

    public Book getBookById(final String id) {
        return bookGatewayMapper.toBookDomain(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("book not found!")));
    }

    public void deleteBookById(final String id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(final String id, BookPutRequestDTO bookPutRequestDTO) {
        Book existingBook = getBookById(id);
        bookMapper.updateUserFromDTO(bookPutRequestDTO, existingBook);
        return bookGatewayMapper.toBookDomain(bookRepository.save(bookGatewayMapper.toBookEntity(existingBook)));
    }

}
