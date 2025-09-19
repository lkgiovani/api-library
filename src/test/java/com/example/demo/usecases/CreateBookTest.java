package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.gateways.BookGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBookTest {

    @Mock
    private BookGateway bookGateway;

    @InjectMocks
    private CreateBook createBook;

    private Book inputBook;
    private Book expectedBook;

    @BeforeEach
    void setUp() {
        inputBook = new Book();
        inputBook.setTitle("Test Title");
        inputBook.setAuthor("Test Author");
        inputBook.setIsbn("978-0123456789");
        inputBook.setStock(10);
        inputBook.setPriceInCents(2999L);

        expectedBook = new Book();
        expectedBook.setId("1");
        expectedBook.setTitle("Test Title");
        expectedBook.setAuthor("Test Author");
        expectedBook.setIsbn("978-0123456789");
        expectedBook.setStock(10);
        expectedBook.setPriceInCents(2999L);
    }

    @Test
    void shouldCreateBookSuccessfully() {
        when(bookGateway.createBook(inputBook)).thenReturn(expectedBook);

        Book result = createBook.execute(inputBook);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getAuthor()).isEqualTo("Test Author");
        assertThat(result.getIsbn()).isEqualTo("978-0123456789");
        assertThat(result.getStock()).isEqualTo(10);
        assertThat(result.getPriceInCents()).isEqualTo(2999L);

        verify(bookGateway, times(1)).createBook(inputBook);
    }

    @Test
    void shouldDelegateToBookGatewayCreateBook() {
        when(bookGateway.createBook(any(Book.class))).thenReturn(expectedBook);

        createBook.execute(inputBook);

        verify(bookGateway).createBook(inputBook);
    }
}
