package com.example.demo.usercases;

import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.repository.BookRepository;
import com.example.demo.usecases.UpdateBook;
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
class UpdateBookTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private UpdateBook updateBook;
    
    private Book existingBook;
    private String bookId;
    
    @BeforeEach
    void setUp() {
        bookId = "123e4567-e89b-12d3-a456-426614174000";
        existingBook = Book.create("Original Title", "Original Author", "978-0123456789", 10, 2999L);
        existingBook.setId(bookId);
    }
    
    @Test
    @DisplayName("Should successfully update all fields when all DTO fields are provided")
    void shouldUpdateAllFieldsWhenAllDtoFieldsProvided() {
        
        BookPutRequestDTO dto = new BookPutRequestDTO(
            "Updated Title", 
            "Updated Author", 
            "978-9876543210", 
            15, 
            3999L
        );
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Book result = updateBook.handle(bookId, dto);
        
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        assertEquals(15, result.getStock());
        assertEquals(3999L, result.getPriceInCents());
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }
    
    @Test
    @DisplayName("Should update only title when only title is provided in DTO")
    void shouldUpdateOnlyTitleWhenOnlyTitleProvided() {
        
        BookPutRequestDTO dto = new BookPutRequestDTO("New Title", null, null, null, null);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Book result = updateBook.handle(bookId, dto);
        
        assertEquals("New Title", result.getTitle());
        assertEquals("Original Author", result.getAuthor()); 
        assertEquals(10, result.getStock()); 
        assertEquals(2999L, result.getPriceInCents()); 
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }
    
    @Test
    @DisplayName("Should update only author when only author is provided in DTO")
    void shouldUpdateOnlyAuthorWhenOnlyAuthorProvided() {
        
        BookPutRequestDTO dto = new BookPutRequestDTO(null, "New Author", null, null, null);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Book result = updateBook.handle(bookId, dto);
        
        assertEquals("Original Title", result.getTitle()); 
        assertEquals("New Author", result.getAuthor());
        assertEquals(10, result.getStock()); 
        assertEquals(2999L, result.getPriceInCents()); 
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }
    
    @Test
    @DisplayName("Should update only stock when only stock is provided in DTO")
    void shouldUpdateOnlyStockWhenOnlyStockProvided() {
        
        BookPutRequestDTO dto = new BookPutRequestDTO(null, null, null, 25, null);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Book result = updateBook.handle(bookId, dto);
        
        assertEquals("Original Title", result.getTitle()); 
        assertEquals("Original Author", result.getAuthor()); 
        assertEquals(25, result.getStock());
        assertEquals(2999L, result.getPriceInCents()); 
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }
    
    @Test
    @DisplayName("Should update only price when only priceInCents is provided in DTO")
    void shouldUpdateOnlyPriceWhenOnlyPriceProvided() {
        
        BookPutRequestDTO dto = new BookPutRequestDTO(null, null, null, null, 4999L);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        
        Book result = updateBook.handle(bookId, dto);
        
        assertEquals("Original Title", result.getTitle()); 
        assertEquals("Original Author", result.getAuthor()); 
        assertEquals(10, result.getStock()); 
        assertEquals(4999L, result.getPriceInCents());
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }
    
    @Test
    @DisplayName("Should not update anything when all DTO fields are null")
    void shouldNotUpdateWhenAllDtoFieldsAreNull() {
        
        BookPutRequestDTO dto = new BookPutRequestDTO(null, null, null, null, null);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Book result = updateBook.handle(bookId, dto);

        assertEquals("Original Title", result.getTitle());
        assertEquals("Original Author", result.getAuthor());
        assertEquals(10, result.getStock());
        assertEquals(2999L, result.getPriceInCents());
        
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }
    
    @Test
    @DisplayName("Should throw CustomHttpException when book is not found")
    void shouldThrowExceptionWhenBookNotFound() {
        
        String nonExistentId = "non-existent-id";
        BookPutRequestDTO dto = new BookPutRequestDTO("New Title", null, null, null, null);
        
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        
        CustomHttpException exception = assertThrows(
            CustomHttpException.class,
            () -> updateBook.handle(nonExistentId, dto)
        );
        
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        
        verify(bookRepository).findById(nonExistentId);
        verify(bookRepository, never()).save(any());
    }
}
