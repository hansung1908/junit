package com.cos.junit.repository;

import com.cos.junit.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository extends시 @Repository 생략 가능
public interface BookRepository extends JpaRepository<Book, Long> {
}
