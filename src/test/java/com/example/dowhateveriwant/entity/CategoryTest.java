package com.example.dowhateveriwant.entity;

import com.example.dowhateveriwant.entity.Category;
import com.example.dowhateveriwant.repository.category.CategoryQuery;
import com.example.dowhateveriwant.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class CategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryQuery categoryQuery;

    @Test
    @Commit
    void init() throws Exception{
        Category category = Category.builder()
                .name("root")
                .build();
        categoryRepository.save(category);
    }

    @Test
    @Commit
    void addFirstLayer() throws Exception{
        Category category = categoryRepository.findById(136L).get();

        List<Category> list = new ArrayList<>();
        String[] name = {"사과","오렌지","딸기"};
        for (int i = 0; i < name.length; i++) {
            list.add(
                    Category.builder()
                    .name(name[i])
                    .path(category.addId())
                    .build());
        }
        categoryRepository.saveAll(list);
    }

    @Test
    @Commit
    void addThirdLayer() throws Exception{
        Category category = categoryRepository.findById(138L).get();
        Category 사과 = categoryRepository.findById(137L).get();
        Category 딸기 = categoryRepository.findById(139L).get();

        Category category1 = Category.builder()
                .name("황금향")
                .path(category.addId())
                .build();
        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .name("천혜향")
                .path(category.addId())
                .build();
        categoryRepository.save(category2);

        Category category3 = Category.builder()
                .name("루비향")
                .path(category.addId())
                .build();
        categoryRepository.save(category3);

        Category category4 = Category.builder()
                .name("아오리사과")
                .path(사과.addId())
                .build();
        categoryRepository.save(category4);

        Category category5 = Category.builder()
                .name("산딸기")
                .path(딸기.addId())
                .build();
        categoryRepository.save(category5);
    }

    @Test
    @Commit
    void addFourthLayer() throws Exception{
        Category 황금향 = categoryRepository.findById(140L).get();
        Category 산딸기 = categoryRepository.findById(144L).get();

        Category category1 = Category.builder()
                .name("산산산딸기")
                .path(산딸기.addId())
                .build();
        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .name("한라봉")
                .path(황금향.addId())
                .build();
        categoryRepository.save(category2);
    }

    @Test
    void 조회() throws Exception{
        Category 오렌지 = categoryRepository.findById(138L).get();
        List<Category> allChildren = categoryQuery.findChildren(오렌지);
        for (Category allChild : allChildren) {
            System.out.println(allChild.getName());
        }

        Category 한라봉 = categoryRepository.findById(146L).get();
        List<Category> parents = categoryQuery.findParents(한라봉);
        for (Category parent : parents) {
            System.out.println(parent.getName() + parent.getName());
        }
    }
}
