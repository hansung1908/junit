package com.cos.junit.service;

import com.cos.junit.dto.BookRespDto;
import com.cos.junit.dto.BookSaveReqDto;
import com.cos.junit.repository.BookRepository;
import com.cos.junit.util.MailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
