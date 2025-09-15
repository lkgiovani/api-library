package com.example.demo.controller.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookPostRequestDTO(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotBlank(message = "ISBN is required")
        String isbn,

        @NotNull(message = "Stock is required")
        Integer stock,

        @NotNull(message = "Price is required")
        Long priceInCents
) {
}
