package com.example.demo.usecases;

import com.example.demo.controller.dtos.BookPutRequestDTO;
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
class UpdateBookTest {

    @Mock
    private BookGateway bookGateway;

    @InjectMocks
    private UpdateBook updateBook;

    private BookPutRequestDTO updateDto;
    private Book updatedBook;

    @BeforeEach
    void setUp() {
        updateDto = new BookPutRequestDTO(
                "Updated Title",
                "Updated Author",
                "978-0987654321",
                15,
                3999L
        );

        updatedBook = new Book();
        updatedBook.setId("1");
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("978-0987654321");
        updatedBook.setStock(15);
        updatedBook.setPriceInCents(3999L);
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        String bookId = "1";
        when(bookGateway.updateBook(bookId, updateDto)).thenReturn(updatedBook);

        Book result = updateBook.execute(bookId, updateDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getAuthor()).isEqualTo("Updated Author");
        assertThat(result.getIsbn()).isEqualTo("978-0987654321");
        assertThat(result.getStock()).isEqualTo(15);
        assertThat(result.getPriceInCents()).isEqualTo(3999L);

        verify(bookGateway, times(1)).updateBook(bookId, updateDto);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        String bookId = "nonexistent";
        when(bookGateway.updateBook(bookId, updateDto))
                .thenThrow(new BookNotFoundException("book not found!"));

        assertThatThrownBy(() -> updateBook.execute(bookId, updateDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found!");

        verify(bookGateway, times(1)).updateBook(bookId, updateDto);
    }

    @Test
    void shouldDelegateToBookGatewayUpdateBook() {
        String bookId = "1";
        when(bookGateway.updateBook(anyString(), any(BookPutRequestDTO.class))).thenReturn(updatedBook);

        updateBook.execute(bookId, updateDto);

        verify(bookGateway).updateBook(bookId, updateDto);
    }
}
