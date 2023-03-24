package com.cos.junit.dto;

import com.cos.junit.domain.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // Controller에서 Setter가 호출되면서 Dto에 값이 채워짐
public class BookSaveReqDto {

    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
