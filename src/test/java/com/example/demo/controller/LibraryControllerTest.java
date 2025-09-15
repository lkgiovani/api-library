package com.example.demo.controller;

import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.domain.Book;
import com.example.demo.exceptions.CustomHttpException;
import com.example.demo.usecases.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryController.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateBook createBook;

    @MockitoBean
    private GetBooks getBooks;

    @MockitoBean
    private GetBook getBook;

    @MockitoBean
    private UpdateBook updateBook;

    @MockitoBean
    private DeleteBook deleteBook;

    private Book book;
    private BookPostRequestDTO postRequestDTO;
    private BookPutRequestDTO putRequestDTO;

    @BeforeEach
    void setUp() {
        book = Book.create("Test Title", "Test Author", "978-0123456789", 10, 2999L);
        book.setId("123e4567-e89b-12d3-a456-426614174000");

        postRequestDTO = new BookPostRequestDTO(
            "Test Title",
            "Test Author", 
            "978-0123456789",
            10,
            2999L
        );

        putRequestDTO = new BookPutRequestDTO(
            "Updated Title",
            "Updated Author",
            "978-9876543210",
            15,
            3999L
        );
    }

    @Test
    @DisplayName("POST /api/books - Should create book successfully")
    void shouldCreateBookSuccessfully() throws Exception {
        when(createBook.handle(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.stock").value(book.getStock()))
                .andExpect(jsonPath("$.priceInCents").value(book.getPriceInCents()));
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 when title is blank")
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        BookPostRequestDTO invalidDTO = new BookPostRequestDTO(
            "",
            "Test Author",
            "978-0123456789",
            10,
            2999L
        );

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 when author is blank")
    void shouldReturn400WhenAuthorIsBlank() throws Exception {
        BookPostRequestDTO invalidDTO = new BookPostRequestDTO(
            "Test Title",
            "",
            "978-0123456789",
            10,
            2999L
        );

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 when ISBN is blank")
    void shouldReturn400WhenIsbnIsBlank() throws Exception {
        BookPostRequestDTO invalidDTO = new BookPostRequestDTO(
            "Test Title",
            "Test Author",
            "",
            10,
            2999L
        );

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 when stock is null")
    void shouldReturn400WhenStockIsNull() throws Exception {
        BookPostRequestDTO invalidDTO = new BookPostRequestDTO(
            "Test Title",
            "Test Author",
            "978-0123456789",
            null,
            2999L
        );

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/books - Should return 400 when price is null")
    void shouldReturn400WhenPriceIsNull() throws Exception {
        BookPostRequestDTO invalidDTO = new BookPostRequestDTO(
            "Test Title",
            "Test Author",
            "978-0123456789",
            10,
            null
        );

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/books - Should return 409 when ISBN already exists")
    void shouldReturn409WhenIsbnAlreadyExists() throws Exception {
        when(createBook.handle(any(Book.class)))
            .thenThrow(new CustomHttpException("Book with ISBN already exists", HttpStatus.CONFLICT.value()));

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("GET /api/books - Should return list of books")
    void shouldReturnListOfBooks() throws Exception {
        Book book1 = Book.create("Title 1", "Author 1", "978-0123456789", 10, 2999L);
        book1.setId("id-1");
        Book book2 = Book.create("Title 2", "Author 2", "978-0123456780", 15, 3999L);
        book2.setId("id-2");
        
        List<Book> books = Arrays.asList(book1, book2);
        when(getBooks.handle()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("id-1"))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].id").value("id-2"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));
    }

    @Test
    @DisplayName("GET /api/books - Should return empty list when no books exist")
    void shouldReturnEmptyListWhenNoBooksExist() throws Exception {
        when(getBooks.handle()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/books/{id} - Should return book when found")
    void shouldReturnBookWhenFound() throws Exception {
        when(getBook.handle(book.getId())).thenReturn(book);

        mockMvc.perform(get("/api/books/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.stock").value(book.getStock()))
                .andExpect(jsonPath("$.priceInCents").value(book.getPriceInCents()));
    }

    @Test
    @DisplayName("GET /api/books/{id} - Should return 404 when book not found")
    void shouldReturn404WhenBookNotFound() throws Exception {
        String nonExistentId = "non-existent-id";
        when(getBook.handle(nonExistentId))
            .thenThrow(new CustomHttpException("Book not found", HttpStatus.NOT_FOUND.value()));

        mockMvc.perform(get("/api/books/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Should update book successfully")
    void shouldUpdateBookSuccessfully() throws Exception {
        Book updatedBook = Book.create("Updated Title", "Updated Author", "978-9876543210", 15, 3999L);
        updatedBook.setId(book.getId());
        
        when(updateBook.handle(book.getId(), putRequestDTO)).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.stock").value(15))
                .andExpect(jsonPath("$.priceInCents").value(3999L));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Should update book with partial data")
    void shouldUpdateBookWithPartialData() throws Exception {
        BookPutRequestDTO partialDTO = new BookPutRequestDTO("New Title", null, null, null, null);
        
        Book partiallyUpdatedBook = Book.create("New Title", "Test Author", "978-0123456789", 10, 2999L);
        partiallyUpdatedBook.setId(book.getId());
        
        when(updateBook.handle(book.getId(), partialDTO)).thenReturn(partiallyUpdatedBook);

        mockMvc.perform(put("/api/books/{id}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.priceInCents").value(2999L));
    }

    @Test
    @DisplayName("PUT /api/books/{id} - Should return 404 when book not found")
    void shouldReturn404WhenUpdatingNonExistentBook() throws Exception {
        String nonExistentId = "non-existent-id";
        when(updateBook.handle(nonExistentId, putRequestDTO))
            .thenThrow(new CustomHttpException("Book not found", HttpStatus.NOT_FOUND.value()));

        mockMvc.perform(put("/api/books/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(putRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Should delete book successfully")
    void shouldDeleteBookSuccessfully() throws Exception {
        doNothing().when(deleteBook).handle(book.getId());

        mockMvc.perform(delete("/api/books/{id}", book.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - Should return 404 when book not found")
    void shouldReturn404WhenDeletingNonExistentBook() throws Exception {
        String nonExistentId = "non-existent-id";
        doThrow(new CustomHttpException("Book not found", HttpStatus.NOT_FOUND.value()))
            .when(deleteBook).handle(nonExistentId);

        mockMvc.perform(delete("/api/books/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle malformed JSON request")
    void shouldHandleMalformedJsonRequest() throws Exception {
        String malformedJson = "{\"title\": \"Test\", \"author\": }";

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle empty request body")
    void shouldHandleEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle unsupported Content-Type header")
    void shouldHandleUnsupportedContentTypeHeader() throws Exception {
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.TEXT_PLAIN)
                .content(objectMapper.writeValueAsString(postRequestDTO)))
                .andExpect(status().isUnsupportedMediaType());
    }
}
