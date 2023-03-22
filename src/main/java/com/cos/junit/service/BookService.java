package com.cos.junit.service;

import com.cos.junit.controller.dto.BookRespDto;
import com.cos.junit.controller.dto.BookSaveReqDto;
import com.cos.junit.domain.Book;
import com.cos.junit.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    // 1. 책 등록
    // bookPS를 응답 시 세션 상단에서 select를 호출할 경우 lazy loading이 된다.
    // 이때 클라이언트 응답하면서 메세지 컨버터는 getter를 통해 json으로 변환하려 할것이며
    // getter로 호출된 book에 foreign key로 연관된 모든 db들이 select되어 불필요한 정보까지 반환하게 된다.
    // 그래서 dto로 변환시켜 방지한다.
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto bookSave(BookSaveReqDto dto) {
        Book bookPs = bookRepository.save(dto.toEntity());
        return new BookRespDto().toDto(bookPs);
    }
}
