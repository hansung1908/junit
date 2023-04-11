package com.cos.junit.controller;

import com.cos.junit.domain.Book;
import com.cos.junit.dto.request.BookSaveReqDto;
import com.cos.junit.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

// 통합 테스트 (c, s, r)
// 컨트롤러만 테스트하는 것이 아닌 모듈 레이어 전체를 테스트
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper om;

    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {

        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach // 각 테스트 시작 전에 한번씩 실행
    public void readyData() {

        String title = "책 제목 테스트";
        String author = "책 저자 테스트";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);
    }

    @Test
    public void bookSaveTest() throws Exception {

        // given
        BookSaveReqDto bookSaveReqDto =  new BookSaveReqDto();
        bookSaveReqDto.setTitle("책 제목 테스트");
        bookSaveReqDto.setAuthor("책 저자 테스트");

        String body = om.writeValueAsString(bookSaveReqDto);


        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("책 제목 테스트");
        assertThat(author).isEqualTo("책 저자 테스트");
    }


    @Sql("classpath:db/tableInit.sql")
    @Test
    public void bookFindAllTest() {

        // given

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("책 제목 테스트");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void bookFindByIdTest() { // 1. bookFindByIdTest 시작 전에 BeforeEach를 시작하는데 이 모든 동작 전에 테이블 초기화를 한번함. (@sql)
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.GET, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("책 제목 테스트");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void bookDeleteTest() {
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, request, String.class);

        // then
        // System.out.println("bookDeleteTest() : " + response.getStatusCode());

        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        assertThat(code).isEqualTo(1);
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void bookUpdateTest() throws Exception {
        // given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("책 제목 테스트");
        bookSaveReqDto.setAuthor("책 저자 테스트");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.PUT, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");

        assertThat(title).isEqualTo("책 제목 테스트");
    }
}
