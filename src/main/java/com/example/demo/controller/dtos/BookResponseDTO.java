package com.example.demo.controller.dtos;

public record BookResponseDTO(
        String id,
        String isbn,
        String title,
        String author,
        Integer stock,
        Long priceInCents
) {
}
