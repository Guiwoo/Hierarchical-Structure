package com.example.dowhateveriwant.repository.category;

import com.example.dowhateveriwant.entity.Category;
import com.example.dowhateveriwant.entity.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.dowhateveriwant.entity.QCategory.category;

@Repository
public class CategoryQuery {
    private JPAQueryFactory queryFactory;
    public CategoryQuery(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Category> findParents(Category cat){

        List<Long> ids = Arrays.stream(cat.getPath().split("@"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return queryFactory.selectFrom(category)
                .where(category.id.in(ids))
                .fetch();
    }

    public List<Category> findChildren(Category cat){
        return queryFactory.selectFrom(category)
                .where(category.path.contains(String.valueOf(cat.getId())))
                .fetch();
    }
}
