package com.example.dowhateveriwant.repository.pizza;

import com.example.dowhateveriwant.entity.Pizza;
import com.example.dowhateveriwant.entity.PizzaPaths;
import com.example.dowhateveriwant.entity.QPizza;
import com.example.dowhateveriwant.entity.QPizzaPaths;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.dowhateveriwant.entity.QPizzaPaths.*;

@Repository
public class PizzaQuery {
    private JPAQueryFactory queryFactory;
    @Autowired
    private PizzaPathsRepository pathsRepository;

    public PizzaQuery(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    public void insertPizza(Pizza p,Pizza parents){
        // 1번 자기자신을 추가해
        savePizzaPath(p);
        // 2번 부모 를 자식으로 참조하는 모든 애들 들고 와서 내아이디 참조로 바꿔

        List<Pizza> fetch = getPizzaByChild(parents);

        List<PizzaPaths> list = new ArrayList<>();
        for (int i = 0; i < fetch.size(); i++) {
            list.add(PizzaPaths.builder()
                    .parents(fetch.get(i))
                    .child(p)
                    .build());
        }

        pathsRepository.saveAll(list);
    }

    public void updateQuery(Pizza pizza,PizzaPaths pp){
        queryFactory.update(pizzaPaths)
                .set(pizzaPaths.child, pizza)
                .where(pizzaPaths.eq(pp))
                .execute();
    }

    public void insertBetween(Pizza brandNew,Pizza current){

        savePizzaPath(brandNew);

        List<Pizza> fetch = queryFactory.select(pizzaPaths.child)
                .from(pizzaPaths)
                .where(pizzaPaths.parents.eq(current))
                .fetch();

        List<PizzaPaths> list = new ArrayList<>();
        for (Pizza paths : fetch) {
            list.add(
                    PizzaPaths.builder()
                            .parents(brandNew)
                            .child(paths)
                            .build()
            );
        }
        updateQuery(current,brandNew);
        pathsRepository.saveAll(list);
    }
    private void updateQuery(Pizza p, Pizza n){
        queryFactory.update(pizzaPaths)
                .set(pizzaPaths.child,n)
                .where(pizzaPaths.child.eq(p),
                        pizzaPaths.child.ne(pizzaPaths.parents))
                .execute();
    }

    private List<Pizza> getPizzaByChild(Pizza parents) {
        return queryFactory.select(pizzaPaths.parents)
                .from(pizzaPaths)
                .where(pizzaPaths.child.id.eq(parents.getId()))
                .fetch();
    }

    private List<Pizza> getPizzaByParetns(Pizza parents) {
        return queryFactory.select(pizzaPaths.parents)
                .from(pizzaPaths)
                .where(pizzaPaths.child.id.eq(parents.getId()),
                        pizzaPaths.child.ne(pizzaPaths.parents))
                .fetch();
    }


    private void savePizzaPath(Pizza p) {
        pathsRepository.save(
                PizzaPaths.builder().parents(p).child(p).build()
        );
    }
}
