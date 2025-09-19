package com.example.demo.usecases;

import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.gateways.BookGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteBookTest {

    @Mock
    private BookGateway bookGateway;

    @InjectMocks
    private DeleteBook deleteBook;

    @Test
    void shouldDeleteBookSuccessfully() {
        String bookId = "1";
        doNothing().when(bookGateway).deleteBookById(bookId);

        deleteBook.execute(bookId);

        verify(bookGateway, times(1)).deleteBookById(bookId);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        String bookId = "nonexistent";
        doThrow(new BookNotFoundException("book not found!"))
                .when(bookGateway).deleteBookById(bookId);

        assertThatThrownBy(() -> deleteBook.execute(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found!");

        verify(bookGateway, times(1)).deleteBookById(bookId);
    }

    @Test
    void shouldDelegateToBookGatewayDeleteById() {
        String bookId = "1";
        doNothing().when(bookGateway).deleteBookById(anyString());

        deleteBook.execute(bookId);

        verify(bookGateway).deleteBookById(bookId);
    }
}
