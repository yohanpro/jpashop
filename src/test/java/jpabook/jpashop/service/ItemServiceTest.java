package jpabook.jpashop.service;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void saveItem() {
        // 테스트 아이템(책) 생성
        Book book = new Book();
        book.setName("Java Basics");
        book.setPrice(30000);
        book.setStockQuantity(100);
        book.setAuthor("John Doe");
        book.setIsbn("1234567890");

        // 아이템 저장
        itemService.saveItem(book);

        // 저장된 아이템 검증
        Item foundItem = itemRepository.findOne(book.getId());
        assertNotNull(foundItem);
        assertInstanceOf(Book.class, foundItem);
        Book foundBook = (Book) foundItem;
        assertEquals(book.getName(), foundBook.getName());
        assertEquals(book.getPrice(), foundBook.getPrice());
        assertEquals(book.getStockQuantity(), foundBook.getStockQuantity());
        assertEquals(book.getAuthor(), foundBook.getAuthor());
        assertEquals(book.getIsbn(), foundBook.getIsbn());
    }

    @Test
    void findItems() {
    }

    @Test
    void findOne() {
        // 테스트 아이템(책) 생성 및 저장
        Book book = new Book();
        book.setName("Advanced Java");
        book.setPrice(45000);
        book.setStockQuantity(50);
        book.setAuthor("Jane Doe");
        book.setIsbn("0987654321");
        itemService.saveItem(book);

        // 아이템 조회
        Item foundItem = itemService.findOne(book.getId());
        assertNotNull(foundItem);
        assertInstanceOf(Book.class, foundItem);
        Book foundBook = (Book) foundItem;
        assertEquals(book.getName(), foundBook.getName());
        assertEquals(book.getAuthor(), foundBook.getAuthor());
        assertEquals(book.getIsbn(), foundBook.getIsbn());
    }
    @Test
    void removeStockWithNotEnoughStock() {
        // 책 객체 생성 및 초기 재고 설정
        Book book = new Book();
        book.setName("Java Concurrency");
        book.setPrice(40000);
        book.setStockQuantity(10); // 재고를 10으로 설정
        book.setAuthor("Brian Goetz");
        book.setIsbn("9780134510690");
        itemService.saveItem(book);

        // 재고가 부족할 때 NotEnoughStockException 예외 발생하는지 테스트
        assertThrows(NotEnoughStockException.class, () -> {
            book.removeStock(20); // 10개 재고에서 20개를 제거하려 함
        });
    }
}