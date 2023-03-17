package com.cos.junit.controller;

import com.cos.junit.domain.Book;

public class BookApiControllerTest {

    public void test() {
        Book book = Book.builder()
                .id(1L)
                .title("제목")
                .author("저자")
                .build();
    }
}
