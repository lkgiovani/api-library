package com.example.demo.gateways.mapper;

import com.example.demo.domain.Book;
import com.example.demo.gateways.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookGatewayMapper {

    BookEntity toBookEntity(Book book);

    Book toBookDomain(BookEntity bookEntity);

}
