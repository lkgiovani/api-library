package com.example.demo.usercases;

import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import com.example.demo.usecases.DeleteBook;
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
class DeleteBookTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private DeleteBook deleteBook;
    
    private Book book;
    private String bookId;
    
    @BeforeEach
    void setUp() {
        bookId = "123e4567-e89b-12d3-a456-426614174000";
        book = Book.create("Test Title", "Test Author", "978-0123456789", 10, 2999L);
        book.setId(bookId);
    }
    
    @Test
    @DisplayName("Should successfully delete book when book exists")
    void shouldDeleteBookWhenBookExists() {

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);
        
  
        assertDoesNotThrow(() -> deleteBook.handle(bookId));
        
  
        verify(bookRepository).findById(bookId);
        verify(bookRepository).deleteById(bookId);
    }
    
    @Test
    @DisplayName("Should throw CustomHttpException when book is not found")
    void shouldThrowExceptionWhenBookNotFound() {

        String nonExistentId = "non-existent-id";
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
  
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> deleteBook.handle(nonExistentId)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(nonExistentId);
        verify(bookRepository, never()).deleteById(any());
    }
    
    @Test
    @DisplayName("Should handle null ID parameter")
    void shouldHandleNullIdParameter() {

        when(bookRepository.findById(null)).thenReturn(Optional.empty());
        
  
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> deleteBook.handle(null)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(null);
        verify(bookRepository, never()).deleteById(any());
    }
    
    @Test
    @DisplayName("Should handle empty string ID parameter")
    void shouldHandleEmptyStringIdParameter() {

        String emptyId = "";
        when(bookRepository.findById(emptyId)).thenReturn(Optional.empty());
        
  
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> deleteBook.handle(emptyId)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(emptyId);
        verify(bookRepository, never()).deleteById(any());
    }
    
    @Test
    @DisplayName("Should verify correct sequence of operations")
    void shouldVerifyCorrectSequenceOfOperations() {

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);
        
  
        deleteBook.handle(bookId);
        
  
        var inOrder = inOrder(bookRepository);
        inOrder.verify(bookRepository).findById(bookId);
        inOrder.verify(bookRepository).deleteById(bookId);
    }
}
