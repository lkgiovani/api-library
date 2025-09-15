package com.example.demo.usercases;

import com.example.demo.domain.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.usecases.GetBooks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBooksTest {
    
    @Mock
    private BookRepository bookRepository;
    
    @InjectMocks
    private GetBooks getBooks;
    
    private List<Book> bookList;
    
    @BeforeEach
    void setUp() {
        Book book1 = Book.create("Title 1", "Author 1", "978-0123456789", 10, 2999L);
        book1.setId("id-1");
        
        Book book2 = Book.create("Title 2", "Author 2", "978-0123456780", 15, 3999L);
        book2.setId("id-2");
        
        Book book3 = Book.create("Title 3", "Author 3", "978-0123456781", 5, 1999L);
        book3.setId("id-3");
        
        bookList = Arrays.asList(book1, book2, book3);
    }
    
    @Test
    @DisplayName("Should return list of all books when books exist")
    void shouldReturnAllBooksWhenBooksExist() {
        when(bookRepository.findAll()).thenReturn(bookList);
        
        List<Book> result = getBooks.handle();
        
        assertNotNull(result);
        assertEquals(3, result.size());
        
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals("978-0123456789", result.get(0).getIsbn());
        assertEquals(10, result.get(0).getStock());
        assertEquals(2999L, result.get(0).getPriceInCents());
        
        assertEquals("Title 2", result.get(1).getTitle());
        assertEquals("Author 2", result.get(1).getAuthor());
        assertEquals("978-0123456780", result.get(1).getIsbn());
        assertEquals(15, result.get(1).getStock());
        assertEquals(3999L, result.get(1).getPriceInCents());
        
        assertEquals("Title 3", result.get(2).getTitle());
        assertEquals("Author 3", result.get(2).getAuthor());
        assertEquals("978-0123456781", result.get(2).getIsbn());
        assertEquals(5, result.get(2).getStock());
        assertEquals(1999L, result.get(2).getPriceInCents());
        
        verify(bookRepository).findAll();
    }
    
    @Test
    @DisplayName("Should return empty list when no books exist")
    void shouldReturnEmptyListWhenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<Book> result = getBooks.handle();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        
        verify(bookRepository).findAll();
    }
    
    @Test
    @DisplayName("Should return single book when only one book exists")
    void shouldReturnSingleBookWhenOnlyOneBookExists() {
        List<Book> singleBookList = Arrays.asList(bookList.get(0));
        when(bookRepository.findAll()).thenReturn(singleBookList);
        
        List<Book> result = getBooks.handle();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Author 1", result.get(0).getAuthor());
        assertEquals("978-0123456789", result.get(0).getIsbn());
        assertEquals(10, result.get(0).getStock());
        assertEquals(2999L, result.get(0).getPriceInCents());
        
        verify(bookRepository).findAll();
    }
    
    @Test
    @DisplayName("Should handle large list of books")
    void shouldHandleLargeListOfBooks() {
        List<Book> largeBookList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Book book = Book.create(
                "Title " + i, 
                "Author " + i, 
                "978-012345678" + i, 
                i, 
                (long) (1000 + i)
            );
            book.setId("id-" + i);
            largeBookList.add(book);
        }
        
        when(bookRepository.findAll()).thenReturn(largeBookList);
        
        List<Book> result = getBooks.handle();
        
        assertNotNull(result);
        assertEquals(100, result.size());
        assertEquals("Title 1", result.get(0).getTitle());
        assertEquals("Title 100", result.get(99).getTitle());
        
        verify(bookRepository).findAll();
    }
    
    @Test
    @DisplayName("Should call repository findAll method exactly once")
    void shouldCallRepositoryFindAllExactlyOnce() {
        when(bookRepository.findAll()).thenReturn(bookList);
        
        getBooks.handle();
        
        verify(bookRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookRepository);
    }
}
