package com.example.demo.controller;

import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.controller.dtos.BookResponseDTO;
import com.example.demo.controller.mapper.BookMapper;
import com.example.demo.domain.Book;
import com.example.demo.exceptions.BookNotFoundException;
import com.example.demo.usecases.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
class LibraryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateBook createBook;

    @MockBean
    private GetBooks getBooks;

    @MockBean
    private GetBook getBook;

    @MockBean
    private UpdateBook updateBook;

    @MockBean
    private DeleteBook deleteBook;

    @MockBean
    private BookMapper bookMapper;

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
    void shouldCreateBookAndReturn201() throws Exception {
        when(bookMapper.toDomain(any(BookPostRequestDTO.class))).thenReturn(book);
        when(createBook.execute(book)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookResponse);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookPostRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value("978-0123456789"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.priceInCents").value(2999));

        verify(bookMapper).toDomain(any(BookPostRequestDTO.class));
        verify(createBook).execute(book);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void shouldReturnBadRequestWhenCreateBookWithInvalidData() throws Exception {
        BookPostRequestDTO invalidRequest = new BookPostRequestDTO(
                "", // Invalid title
                "",
                "",
                null,
                null
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(createBook, never()).execute(any(Book.class));
    }

    @Test
    void shouldGetAllBooksAndReturn200() throws Exception {
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

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].title").value("Book Two"));

        verify(getBooks).execute();
        verify(bookMapper, times(2)).toDTO(any(Book.class));
    }

    @Test
    void shouldGetBookByIdAndReturn200() throws Exception {
        String bookId = "1";
        when(getBook.execute(bookId)).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookResponse);

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"));

        verify(getBook).execute(bookId);
        verify(bookMapper).toDTO(book);
    }

    @Test
    void shouldReturn500WhenBookNotFound() throws Exception {
        String bookId = "nonexistent";
        when(getBook.execute(bookId)).thenThrow(new BookNotFoundException("book not found!"));

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isInternalServerError());

        verify(getBook).execute(bookId);
    }

    @Test
    void shouldUpdateBookAndReturn200() throws Exception {
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

        when(updateBook.execute(eq(bookId), any(BookPutRequestDTO.class))).thenReturn(updatedBook);
        when(bookMapper.toDTO(updatedBook)).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookPutRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"));

        verify(updateBook).execute(eq(bookId), any(BookPutRequestDTO.class));
        verify(bookMapper).toDTO(updatedBook);
    }

    @Test
    void shouldDeleteBookAndReturn200() throws Exception {
        String bookId = "1";
        doNothing().when(deleteBook).execute(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isOk());

        verify(deleteBook).execute(bookId);
    }

    @Test
    void shouldReturn500WhenDeletingNonexistentBook() throws Exception {
        String bookId = "nonexistent";
        doThrow(new BookNotFoundException("book not found!")).when(deleteBook).execute(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isInternalServerError());

        verify(deleteBook).execute(bookId);
    }
}
