package com.example.demo.usercases;

import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import com.example.demo.usecases.GetBook;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private GetBook getBook;
    
    private Book book;
    private String bookId;
    
    @BeforeEach
    void setUp() {
        bookId = "123e4567-e89b-12d3-a456-426614174000";
        book = Book.create("Test Title", "Test Author", "978-0123456789", 10, 2999L);
        book.setId(bookId);
    }
    
    @Test
    @DisplayName("Should successfully return book when book exists")
    void shouldReturnBookWhenBookExists() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        Book result = getBook.handle(bookId);
        
        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getAuthor(), result.getAuthor());
        assertEquals(book.getIsbn(), result.getIsbn());
        assertEquals(book.getStock(), result.getStock());
        assertEquals(book.getPriceInCents(), result.getPriceInCents());
        
        verify(bookRepository).findById(bookId);
    }
    
    @Test
    @DisplayName("Should throw CustomHttpException when book is not found")
    void shouldThrowExceptionWhenBookNotFound() {
        String nonExistentId = "non-existent-id";
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> getBook.handle(nonExistentId)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(nonExistentId);
    }
    
    @Test
    @DisplayName("Should handle null ID parameter")
    void shouldHandleNullIdParameter() {
        when(bookRepository.findById(null)).thenReturn(Optional.empty());
        
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> getBook.handle(null)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(null);
    }
    
    @Test
    @DisplayName("Should handle empty string ID parameter")
    void shouldHandleEmptyStringIdParameter() {
        String emptyId = "";
        when(bookRepository.findById(emptyId)).thenReturn(Optional.empty());
        
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> getBook.handle(emptyId)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(emptyId);
    }
}
