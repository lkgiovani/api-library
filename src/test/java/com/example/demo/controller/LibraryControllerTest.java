package com.example.demo.controller;

import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.controller.dtos.BookResponseDTO;
import com.example.demo.controller.mapper.BookMapper;
import com.example.demo.domain.Book;
import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.usecases.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {

    @Mock
    private CreateBook createBook;

    @Mock
    private GetBooks getBooks;

    @Mock
    private GetBook getBook;

    @Mock
    private UpdateBook updateBook;

    @Mock
    private DeleteBook deleteBook;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private LibraryController libraryController;

    private BookPostRequestDTO bookPostRequest;
    private BookPutRequestDTO bookPutRequest;
    private BookResponseDTO bookResponse;
    private Book book;

    @BeforeEach
    void setUp() {
        bookPostRequest = new BookPostRequestDTO(
                "Test Title",
                "Test Author",
                "978-0123456789",
                10,
                2999L
        );

        bookPutRequest = new BookPutRequestDTO(
                "Updated Title",
                "Updated Author",
                "978-9876543210",
                15,
                3999L
        );

        book = new Book();
        book.setId("1");
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setIsbn("978-0123456789");
        book.setStock(10);
        book.setPriceInCents(2999L);

        bookResponse = new BookResponseDTO(
                "1",
                "978-0123456789",
                "Test Title",
                "Test Author",
                10,
                2999L
        );
    }

    @Test
    void shouldCreateBookSuccessfully() {
        when(bookMapper.toDomain(bookPostRequest)).thenReturn(book);
        when(createBook.execute(book)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookResponse);

        BookResponseDTO result = libraryController.createBook(bookPostRequest);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        assertThat(result.title()).isEqualTo("Test Title");
        assertThat(result.author()).isEqualTo("Test Author");
        assertThat(result.isbn()).isEqualTo("978-0123456789");
        assertThat(result.stock()).isEqualTo(10);
        assertThat(result.priceInCents()).isEqualTo(2999L);

        verify(bookMapper).toDomain(bookPostRequest);
        verify(createBook).execute(book);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void shouldGetAllBooksSuccessfully() {
        Book book2 = new Book();
        book2.setId("2");
        book2.setTitle("Book Two");
        book2.setAuthor("Author Two");
        book2.setIsbn("978-9876543210");
        book2.setStock(5);
        book2.setPriceInCents(1999L);

        BookResponseDTO bookResponse2 = new BookResponseDTO(
                "2",
                "978-9876543210",
                "Book Two",
                "Author Two",
                5,
                1999L
        );

        List<Book> books = Arrays.asList(book, book2);
        
        when(getBooks.execute()).thenReturn(books);
        when(bookMapper.toDTO(book)).thenReturn(bookResponse);
        when(bookMapper.toDTO(book2)).thenReturn(bookResponse2);

        List<BookResponseDTO> result = libraryController.getBooks();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo("1");
        assertThat(result.get(0).title()).isEqualTo("Test Title");
        assertThat(result.get(1).id()).isEqualTo("2");
        assertThat(result.get(1).title()).isEqualTo("Book Two");

        verify(getBooks).execute();
        verify(bookMapper, times(2)).toDTO(any(Book.class));
    }

    @Test
    void shouldGetBookByIdSuccessfully() {
        String bookId = "1";
        when(getBook.execute(bookId)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookResponse);

        BookResponseDTO result = libraryController.getBook(bookId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        assertThat(result.title()).isEqualTo("Test Title");
        assertThat(result.author()).isEqualTo("Test Author");

        verify(getBook).execute(bookId);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        String bookId = "nonexistent";
        when(getBook.execute(bookId)).thenThrow(new BookNotFoundException("book not found!"));

        assertThatThrownBy(() -> libraryController.getBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found!");

        verify(getBook).execute(bookId);
        verify(bookMapper, never()).toDTO(any(Book.class));
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        String bookId = "1";
        Book updatedBook = new Book();
        updatedBook.setId("1");
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("978-9876543210");
        updatedBook.setStock(15);
        updatedBook.setPriceInCents(3999L);

        BookResponseDTO updatedResponse = new BookResponseDTO(
                "1",
                "978-9876543210",
                "Updated Title",
                "Updated Author",
                15,
                3999L
        );

        when(updateBook.execute(bookId, bookPutRequest)).thenReturn(updatedBook);
        when(bookMapper.toDTO(updatedBook)).thenReturn(updatedResponse);

        BookResponseDTO result = libraryController.updateBook(bookId, bookPutRequest);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("1");
        assertThat(result.title()).isEqualTo("Updated Title");
        assertThat(result.author()).isEqualTo("Updated Author");
        assertThat(result.isbn()).isEqualTo("978-9876543210");
        assertThat(result.stock()).isEqualTo(15);
        assertThat(result.priceInCents()).isEqualTo(3999L);

        verify(updateBook).execute(bookId, bookPutRequest);
        verify(bookMapper).toDTO(updatedBook);
    }

    @Test
    void shouldDeleteBookSuccessfully() {
        String bookId = "1";
        doNothing().when(deleteBook).execute(bookId);

        libraryController.deleteBook(bookId);

        verify(deleteBook).execute(bookId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentBook() {
        String bookId = "nonexistent";
        doThrow(new BookNotFoundException("book not found!")).when(deleteBook).execute(bookId);

        assertThatThrownBy(() -> libraryController.deleteBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found!");

        verify(deleteBook).execute(bookId);
    }
}
