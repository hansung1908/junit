package com.cos.junit.service;

import com.cos.junit.dto.BookRespDto;
import com.cos.junit.dto.BookSaveReqDto;
import com.cos.junit.domain.Book;
import com.cos.junit.repository.BookRepository;
import com.cos.junit.util.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MailSender mailSender;

    // 1. 책 등록
    // bookPS를 응답 시 세션 상단에서 select를 호출할 경우 lazy loading이 된다.
    // 이때 클라이언트 응답하면서 메세지 컨버터는 getter를 통해 json으로 변환하려 할것이며
    // getter로 호출된 book에 foreign key로 연관된 모든 db들이 select되어 불필요한 정보까지 반환하게 된다.
    // 그래서 dto로 변환시켜 방지한다.
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto bookSave(BookSaveReqDto dto) {
        Book bookPS = bookRepository.save(dto.toEntity());
        if(bookPS != null) {
            if(!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다.");
            }
        }
        return new BookRespDto().toDto(bookPS);
    }

    // 2. 책 목록 보기
    public List<BookRespDto> bookFindAll() {
        return bookRepository.findAll().stream()
                .map(new BookRespDto()::toDto)
                .collect(Collectors.toList());
    }

    // 3. 책 한건 보기
    public BookRespDto bookFindById(Long id) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()) {
            return new BookRespDto().toDto(bookOP.get());
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }

    // 4. 책 삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void bookDelete(Long id) {
        bookRepository.deleteById(id);
    }

    // 5. 책 수정
    @Transactional(rollbackFor = RuntimeException.class)
    public void bookUpdate(Long id, BookRespDto dto) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if(bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(), dto.getAuthor());
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    } // 메서드 종료 시 더티체킹(flush)으로 update

}
