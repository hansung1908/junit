package com.cos.junit.controller;

import com.cos.junit.dto.request.BookSaveReqDto;
import com.cos.junit.dto.response.BookListRespDto;
import com.cos.junit.dto.response.BookRespDto;
import com.cos.junit.dto.response.CMRespDto;
import com.cos.junit.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class BookApiController { // 컴포지션 = has 관계

    private final BookService bookService; // fianl을 붙이면 꼭 초기화 해야됨. 그래서 @RequiredArgsConstructor 등록해서 생성자에서 초기화

    // 1. 책 등록
    // key = value & key = value
    // { "key" : value, "key" : value }
    @PostMapping("/api/v1/book")
    private ResponseEntity<?> bookSave(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {

        // aop 처리가 좋음
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            System.out.println("==============================");
            System.out.println(errorMap.toString());
            System.out.println("==============================");

            throw new RuntimeException(errorMap.toString());
        }

        BookRespDto bookRespDto = bookService.bookSave(bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 저장 성공").body(bookRespDto).build(), HttpStatus.CREATED); // 201 = insert
    }

    @PostMapping("/api/v2/book")
    private ResponseEntity<?> bookSaveV2(@RequestBody BookSaveReqDto bookSaveReqDto) {

        BookRespDto bookRespDto = bookService.bookSave(bookSaveReqDto);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 저장 성공").body(bookRespDto).build(), HttpStatus.CREATED); // 201 = insert
    }

    // 2. 책 목록 보기
    @GetMapping("/api/v1/book")
    private ResponseEntity<?> bookFindAll() {
        BookListRespDto bookListRespDto = bookService.bookFindAll();
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("책 목록 보기 성공").body(bookListRespDto).build(), HttpStatus.OK); // 200 = OK
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
