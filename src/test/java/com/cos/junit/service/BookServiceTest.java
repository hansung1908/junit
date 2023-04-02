package com.cos.junit.service;

import com.cos.junit.domain.Book;
import com.cos.junit.dto.response.BookListRespDto;
import com.cos.junit.dto.response.BookRespDto;
import com.cos.junit.dto.request.BookSaveReqDto;
import com.cos.junit.repository.BookRepository;
import com.cos.junit.mail.MailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    // mock을 사용해 가짜 레포지토리 환경을 만들어 서비스 레이어 테스트만 진행
    @Test
    public void bookSaveTest() {

        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("테스트 제목");
        dto.setAuthor("테스트 저자");

        // stub (가설)
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        // when
        BookRespDto bookRespDto = bookService.bookSave(dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());
    }

    // 체크 포인트
    @Test
    public void bookFindAllTest() {
        // given (파라메터로 들어올 데이터)

        // stub (가설)
        List<Book> books = Arrays.asList(
                new Book(1L, "첫번째 테스트 제목", "첫번째 테스트 저자"),
                new Book(2L, "두번째 테스트 제목", "두번째 테스트 저자")
        );
        when(bookRepository.findAll()).thenReturn(books);

        // when (실행)
        BookListRespDto bookListRespDto = bookService.bookFindAll();

        // then (검증)
        assertThat(bookListRespDto.getItems().get(0).getTitle()).isEqualTo("첫번째 테스트 제목");
        assertThat(bookListRespDto.getItems().get(0).getAuthor()).isEqualTo("첫번째 테스트 저자");
        assertThat(bookListRespDto.getItems().get(1).getTitle()).isEqualTo("두번째 테스트 제목");
        assertThat(bookListRespDto.getItems().get(1).getAuthor()).isEqualTo("두번째 테스트 저자");
    }

    @Test
    public void bookFindByIdTest() {
        // given
        Long id = 1L;
        Book book =  new Book(1L, "테스트 제목", "테스트 저자");
        Optional<Book> bookOP = Optional.of(book);

        // stub
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.bookFindById(id);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void bookUpdateTest() {
        // given
        Long id = 1L;
        BookRespDto dto = new BookRespDto();
        dto.setTitle("테스트 제목");
        dto.setAuthor("테스트 저자");

        // stub
        Book book =  new Book(1L, "테스트 제목", "테스트 저자");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        // when
        BookRespDto bookRespDto = bookService.bookUpdate(id, dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    // 책 삭제하기 테스트는 db 삭제 로직만 있어서 테스트 x
}
