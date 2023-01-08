package com.example.dowhateveriwant.repository.pizza;

import com.example.dowhateveriwant.entity.Pizza;
import com.example.dowhateveriwant.entity.PizzaPaths;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PizzaPathsRepository extends JpaRepository<PizzaPaths,Long> {
    List<PizzaPaths> findAllByChild(Pizza pizza);
    void deleteAllByChild(Pizza pizza);

    @Query(value = "insert into pizza_paths (parents,child) " +
            "(select super.parents,sub.child from pizza_paths super" +
            " cross join pizza_paths sub" +
            " where super.child = parents and sub.parents = child)",nativeQuery = true)
    void shitInsertData(@Param("parents") Long parents,@Param("child") Long child);
}
