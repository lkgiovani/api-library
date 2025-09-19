package com.example.demo.controller.mapper;

import com.example.demo.controller.dtos.BookPostRequestDTO;
import com.example.demo.controller.dtos.BookPutRequestDTO;
import com.example.demo.controller.dtos.BookResponseDTO;
import com.example.demo.domain.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toDomain(BookPostRequestDTO bookPostRequestDTO);

    BookResponseDTO toDTO(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateUserFromDTO(BookPutRequestDTO bookPutRequestDTO, @MappingTarget Book book);

}
