package com.example.demo.usercases;

import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import com.example.demo.usecases.CreateBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBookTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private CreateBook createBook;
    
    private Book book;
    
    @BeforeEach
    void setUp() {
        book = Book.create("Test Title", "Test Author", "978-0123456789", 10, 2999L);
    }
    
    @Test
    @DisplayName("Should successfully create a book when ISBN doesn't exist")
    void shouldCreateBookWhenIsbnDoesntExist() {
        
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        Book result = createBook.handle(book);
      
        assertNotNull(result);
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getIsbn(), result.getIsbn());
        assertEquals(book.getStock(), result.getStock());
        assertEquals(book.getPriceInCents(), result.getPriceInCents());
        
        verify(bookRepository).findById(book.getIsbn());
        verify(bookRepository).save(book);
    }
    
    @Test
    @DisplayName("Should throw CustomHttpException when ISBN already exists")
    void shouldThrowExceptionWhenIsbnAlreadyExists() {
      
        Book existingBook = Book.create("Existing Title", "Existing Author", "978-0123456789", 5, 1999L);
        when(bookRepository.findById(book.getIsbn())).thenReturn(Optional.of(existingBook));
        
  
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> createBook.handle(book)
        );
        
        assertEquals("Book with ISBN already exists: " + book.getIsbn(), exception.getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(book.getIsbn());
        verify(bookRepository, never()).save(any());
    }
}
