package com.example.demo.usecases;

import com.example.demo.domain.Book;
import com.example.demo.gateways.BookGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBooksTest {

    @Mock
    private BookGateway bookGateway;

    @InjectMocks
    private GetBooks getBooks;

    private List<Book> expectedBooks;

    @BeforeEach
    void setUp() {
        Book book1 = new Book();
        book1.setId("1");
        book1.setTitle("Book One");
        book1.setAuthor("Author One");
        book1.setIsbn("978-0123456789");
        book1.setStock(10);
        book1.setPriceInCents(2999L);

        Book book2 = new Book();
        book2.setId("2");
        book2.setTitle("Book Two");
        book2.setAuthor("Author Two");
        book2.setIsbn("978-9876543210");
        book2.setStock(5);
        book2.setPriceInCents(1999L);

        expectedBooks = Arrays.asList(book1, book2);
    }

    @Test
    void shouldGetAllBooksSuccessfully() {
        when(bookGateway.listAll()).thenReturn(expectedBooks);

        List<Book> result = getBooks.execute();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(0).getTitle()).isEqualTo("Book One");
        assertThat(result.get(1).getId()).isEqualTo("2");
        assertThat(result.get(1).getTitle()).isEqualTo("Book Two");

        verify(bookGateway, times(1)).listAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoBooksFound() {
        when(bookGateway.listAll()).thenReturn(Collections.emptyList());

        List<Book> result = getBooks.execute();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(bookGateway, times(1)).listAll();
    }

    @Test
    void shouldDelegateToBookGatewayListAll() {
        when(bookGateway.listAll()).thenReturn(expectedBooks);

        getBooks.execute();

        verify(bookGateway).listAll();
    }
}
