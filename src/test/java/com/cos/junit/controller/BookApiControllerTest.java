package com.cos.junit.controller;

import com.cos.junit.dto.request.BookSaveReqDto;
import com.cos.junit.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

// 통합 테스트 (c, s, r)
// 컨트롤러만 테스트하는 것이 아닌 모듈 레이어 전체를 테스트
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookService bookService;

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


}
