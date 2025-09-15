package com.example.demo.controller.dtos;

public record BookPutRequestDTO(
        String title,
        String author,
        String isbn,
        Integer stock,
        Long priceInCents
) {
}