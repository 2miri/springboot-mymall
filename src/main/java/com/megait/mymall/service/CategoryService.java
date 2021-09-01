package com.megait.mymall.service;

import com.megait.mymall.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /*
    17개의 카테고리 만들어보장
    - 도서
        - 국내도서
            - 소설
            - 자기계발/교양
            - 교육
            - 경영

       - 해외도서
            - 소설
            - 자기계발/교양
            - 교육
            - 경영

    - 음반
        - KPOP
        - POP
        - 클래식
            - 성악
            - 악기

     */
    @PostConstruct
    public void createCategories(){

    }
}
