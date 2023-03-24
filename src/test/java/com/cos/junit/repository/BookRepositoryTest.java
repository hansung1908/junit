package com.cos.junit.repository;

import com.cos.junit.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired // DI
    private BookRepository bookRepository;

    // @BeforeAll // 테스트 시작 전에 한번만 실행
    @BeforeEach // 각 테스트 시작 전에 한번씩 실행
    public void readyData() {

        String title = "테스트 제목";
        String author = "테스트 저자";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.save(book);
    } // 트랜잭션 종료 했다면 말이 안됨
    // [데이터 준비() + 1 책등록] (T), [데이터 준비() + 2 책등록보기] (T)

    // 1. 책 등록
    @Test
    public void saveTest() {

        // given (데이터 준비)
        String title = "테스트 제목";
        String author = "테스트 저자";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트 실행)
        Book bookPs = bookRepository.save(book);

        // then (검증)
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화함)

    // 2. 책 목록 보기
    @Test
    public void findAllTest() {

        // given
        String title = "테스트 제목";
        String author = "테스트 저자";

        // when
        List<Book> booksPS = bookRepository.findAll();

        // then
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    } // 트랜잭션 종료 (저장된 데이터를 초기화함)

    // 3. 책 한건 보기
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void findByIdTest() {

        // given
        String title = "테스트 제목";
        String author = "테스트 저자";

        // when
        Book bookPS = bookRepository.findById(1L).get();

        // then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }

    // 4. 책 삭제
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteTest() {

        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        assertFalse(bookRepository.findById(id).isPresent());
    }

    // 5. 책 수정
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void updateTest() {

        // given
        Long id = 1L;
        String title = "수정 테스트 제목";
        String author = "수정 테스트 저자";
        Book book = new Book(id ,title ,author);

        // when
        Book bookPs = bookRepository.save(book);

//        bookRepository.findAll().stream()
//                .forEach(b -> {
//                    System.out.println(b.getId());
//                    System.out.println(b.getTitle());
//                    System.out.println(b.getAuthor());
//                    System.out.println("----------------");
//                });

        // then
        assertEquals(id, bookPs.getId());
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());
    }
}
