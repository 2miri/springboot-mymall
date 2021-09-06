package com.megait.mymall.service;

import com.megait.mymall.domain.Album;
import com.megait.mymall.domain.Book;
import com.megait.mymall.domain.Category;
import com.megait.mymall.repository.AlbumRepository;
import com.megait.mymall.repository.BookRepository;
import com.megait.mymall.repository.CategoryRepository;
import com.megait.mymall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final BookRepository bookRepository;
    private final AlbumRepository albumRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    // 카테고리가 먼저 생기고나서 아이템서비스가 생겨야하므로
    // 카테고리 서비스 선언해두는 것임

    @PostConstruct
    public void createItems() throws IOException{
        log.info("createItems()");
        createAlbumItems();
        createBookItems();
    }

    @Transactional
    public void createAlbumItems() throws IOException {
        log.info("createAlbumItems()");
        // classpath/csv/book.CSV
        ClassPathResource resource2 = new ClassPathResource("csv/album.CSV");

        // 문자열을 모두 받아 List 에 담음. (구분 : 줄바꿈)
        List<String> stringList2 =
                Files.readAllLines(resource2.getFile().toPath(), StandardCharsets.UTF_8);
        Category category = null;
        for(String s : stringList2){
            category = categoryRepository.findById((long)(Math.random() * 3) + 5).orElseThrow();

            String[] split = s.split("\\|"); //  '\\|' : 정규식에서의 '|'
            Album album = Album.builder()
                    .name(split[0]) // 상품명
                    .imageUrl(split[1]) // 이미지 경로
                    .price(Integer.parseInt(split[2])) // 가격
                    .stockQuantity((int)(Math.random() * 10)) // 수량 (stock) - 0 ~ 9 랜덤
                    .build();

            album = albumRepository.save(album);
            album.setCategory(category);
        }
    }

    @Transactional
    public void createBookItems() throws IOException {
        log.info("createBookItems()");
        // classpath/csv/book.CSV
        ClassPathResource resource1 = new ClassPathResource("csv/book.CSV");
        // 문자열을 모두 받아 List 에 담음. (구분 : 줄바꿈)
        List<String> stringList =
                Files.readAllLines(resource1.getFile().toPath(), StandardCharsets.UTF_8);
        /*
            stringList = { "책이름|url|가격", "책이름|url|가격", "책이름|url|가격", ... }

         */

        // forEach : 컨슈머형으로 사용하는 for문
        stringList.forEach(s -> {
            Category category = categoryRepository.findById((long)(Math.random() * 4) + 8).orElseThrow();
            String[] split = s.split("\\|"); //  '\\|' : 정규식에서의 '|'

            Book book = Book.builder()
                    .name(split[0]) // 상품명
                    .imageUrl(split[1]) // 이미지 경로
                    .price(Integer.parseInt(split[2])) // 가격
                    .stockQuantity((int) (Math.random() * 10)) // 수량 (stock) - 0 ~ 9 랜덤
                    .build();

            bookRepository.save(book);
            book.setCategory(category);

        });
    }


    public List<Book> getBookList() {
        return bookRepository.findAll();
    }

    public List<Album> getAlbumList() {
        return albumRepository.findAll();
    }

}