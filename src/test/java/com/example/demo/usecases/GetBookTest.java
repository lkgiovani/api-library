package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.gateways.BookGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBookTest {

    @Mock
    private BookGateway bookGateway;

    @InjectMocks
    private GetBook getBook;

    private Book expectedBook;

    @BeforeEach
    void setUp() {
        expectedBook = new Book();
        expectedBook.setId("1");
        expectedBook.setTitle("Test Title");
        expectedBook.setAuthor("Test Author");
        expectedBook.setIsbn("978-0123456789");
        expectedBook.setStock(10);
        expectedBook.setPriceInCents(2999L);
    }

    @Test
    void shouldGetBookByIdSuccessfully() {
        String bookId = "1";
        when(bookGateway.getBookById(bookId)).thenReturn(expectedBook);

        Book result = getBook.execute(bookId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getTitle()).isEqualTo("Test Title");
        assertThat(result.getAuthor()).isEqualTo("Test Author");
        assertThat(result.getIsbn()).isEqualTo("978-0123456789");
        assertThat(result.getStock()).isEqualTo(10);
        assertThat(result.getPriceInCents()).isEqualTo(2999L);

        verify(bookGateway, times(1)).getBookById(bookId);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        String bookId = "nonexistent";
        when(bookGateway.getBookById(bookId)).thenThrow(new BookNotFoundException("book not found!"));

        assertThatThrownBy(() -> getBook.execute(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found!");

        verify(bookGateway, times(1)).getBookById(bookId);
    }

    @Test
    void shouldDelegateToBookGatewayGetById() {
        String bookId = "1";
        when(bookGateway.getBookById(anyString())).thenReturn(expectedBook);

        getBook.execute(bookId);

        verify(bookGateway).getBookById(bookId);
    }
}
