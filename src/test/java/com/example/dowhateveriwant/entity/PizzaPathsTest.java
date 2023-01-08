package com.example.dowhateveriwant.entity;

import com.example.dowhateveriwant.repository.pizza.PizzaPathsRepository;
import com.example.dowhateveriwant.repository.pizza.PizzaQuery;
import com.example.dowhateveriwant.repository.pizza.PizzaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PizzaPathsTest {

    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private PizzaPathsRepository pizzaPathsRepository;
    @Autowired
    private PizzaQuery pizzaQuery;
    @Autowired
    private EntityManager em;


    @BeforeEach
    void init(){
        Pizza 토마토피자 = Pizza.builder().name("토마토피자").build();
        Pizza 마르게리타 = Pizza.builder().name("마르게리타").build();
        Pizza 살라미 = Pizza.builder().name("살라미").build();

        pizzaRepository.saveAll(List.of(토마토피자,마르게리타,살라미));

        PizzaPaths selfA = PizzaPaths.builder()
                .parents(토마토피자)
                .child(토마토피자)
                .build();

        PizzaPaths selfB = PizzaPaths.builder()
                .parents(마르게리타)
                .child(마르게리타)
                .build();

        PizzaPaths path1 = PizzaPaths.builder()
                .parents(살라미)
                .child(살라미)
                .build();

        PizzaPaths path2 = PizzaPaths.builder()
                .parents(토마토피자)
                .child(마르게리타)
                .build();

        PizzaPaths path3 = PizzaPaths.builder()
                .parents(토마토피자)
                .child(살라미)
                .build();

        pizzaPathsRepository.saveAll(List.of(selfA,selfB,path1,path2,path3));
    }

    @Test
    void uniqueTest(){
        Pizza a = pizzaRepository.findByName("토마토피자").get();
        Pizza b = pizzaRepository.findByName("마르게리타").get();

        PizzaPaths selfA = PizzaPaths.builder()
                .parents(a)
                .child(a)
                .build();

        PizzaPaths selfB = PizzaPaths.builder()
                .parents(b)
                .child(b)
                .build();

        PizzaPaths path1 = PizzaPaths.builder()
                .parents(a)
                .child(b)
                .build();

        PizzaPaths path2 = PizzaPaths.builder()
                .parents(a)
                .child(b)
                .build();

        pizzaPathsRepository.saveAll(List.of(selfA,selfB,path1,path2));
        Assertions.assertThrows(DataIntegrityViolationException.class,
                ()->pizzaPathsRepository.findAll());
    }

    @Test
    void testUpdate() throws Exception{
        Pizza test = pizzaRepository.save(Pizza.builder()
                .name("test")
                .build());
        List<PizzaPaths> all = pizzaPathsRepository.findAll();
        PizzaPaths pizzaPaths = all.get(0);
        pizzaQuery.updateQuery(test,pizzaPaths);

        em.flush();
        em.clear();

        PizzaPaths byId = pizzaPathsRepository.findById(pizzaPaths.getId()).get();
        System.out.println(byId);
        System.out.println(test);
        Assertions.assertEquals(byId.getChild().getId(),test.getId());
    }

    @Test
    void insertEndBetweeen() throws Exception{

        Pizza 살라미 = pizzaRepository.findByName("살라미").get();
        Pizza save = pizzaRepository.save(Pizza.builder().name("디아볼로").build());
        Pizza save2 = pizzaRepository.save(Pizza.builder().name("하와이안").build());
        Pizza between = pizzaRepository.save(Pizza.builder().name("중간낑겨").build());
        pizzaQuery.insertPizza(save,살라미);
        pizzaQuery.insertPizza(save2,살라미);
        pizzaQuery.insertBetween(between,살라미);

        em.flush();
        em.clear();

        List<PizzaPaths> all = pizzaPathsRepository.findAll();
        for (PizzaPaths pizzaPaths : all) {
            System.out.println(
                    pizzaPaths.getId() + " "+ pizzaPaths.getParents().getName() + " "
                    + pizzaPaths.getChild().getName()
            );
        }
    }

    @Test
    void moveSubTreeToOther() throws Exception{
        insert();
        // 살라미 를 마르게리타 아래로 옮길꺼야 어떻게 ? 부모 를 지워주자.
        Pizza 마르게리타 = pizzaRepository.findByName("마르게리타").get();
        Pizza 살라미 = pizzaRepository.findByName("살라미").get();

        pizzaQuery.moveWithSubTree(살라미,마르게리타);

        pizzaPathsRepository.shitInsertData(마르게리타.getId(),살라미.getId());

        List<PizzaPaths> all = pizzaPathsRepository.findAll();
        for (PizzaPaths pizzaPaths : all) {
            System.out.println(pizzaPaths.getParents()
                    +" "+ pizzaPaths.getChild());
        }
    }

    private void insert() {
        Pizza 살라미 = pizzaRepository.findByName("살라미").get();
        Pizza save = pizzaRepository.save(Pizza.builder().name("디아볼로").build());
        Pizza save2 = pizzaRepository.save(Pizza.builder().name("하와이안").build());
        pizzaQuery.insertPizza(save,살라미);
        pizzaQuery.insertPizza(save2,살라미);
    }
}