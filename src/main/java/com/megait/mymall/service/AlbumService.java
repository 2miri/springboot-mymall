package com.megait.mymall.service;

import com.megait.mymall.domain.Album;
import com.megait.mymall.domain.Book;
import com.megait.mymall.domain.Category;
import com.megait.mymall.repository.AlbumRepository;
import com.megait.mymall.repository.BookRepository;
import com.megait.mymall.repository.CategoryRepository;
import com.megait.mymall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

//@Service
@Transactional
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final CategoryRepository categoryRepository;


}
