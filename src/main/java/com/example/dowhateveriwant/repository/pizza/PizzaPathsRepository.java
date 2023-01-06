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
}