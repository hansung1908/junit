package com.cos.junit.controller;

import com.cos.junit.dto.response.BookRespDto;
import com.cos.junit.dto.request.BookSaveReqDto;
import com.cos.junit.dto.response.CMRespDto;
import com.cos.junit.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookApiController { // 컴포지션 = has 관계

    private final BookService bookService; // fianl을 붙이면 꼭 초기화 해야됨. 그래서 @RequiredArgsConstructor 등록해서 생성자에서 초기화

    // 1. 책 등록
    // key = value & key = value
    // { "key" : value, "key" : value }
    @PostMapping("/api/v1/book")
    private ResponseEntity<?> bookSave(@RequestBody BookSaveReqDto bookSaveReqDto) {
        BookRespDto bookRespDto = bookService.bookSave(bookSaveReqDto);
        CMRespDto<?> cmRespDto = CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRespDto).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책 목록 보기
    private ResponseEntity<?> bookFindAll() {
        return null;
    }

    // 3. 책 한건 보기
    private ResponseEntity<?> BookFindById() {
        return null;
    }

    // 4. 책 삭제
    private ResponseEntity<?> bookDelete() {
        return null;
    }

    // 5. 책 수정
    private ResponseEntity<?> bookUpdate() {
        return null;
    }
}
