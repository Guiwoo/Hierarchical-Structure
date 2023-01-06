package com.example.dowhateveriwant.repository.pizza;

import com.example.dowhateveriwant.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza,Long> {

    Optional<Pizza> findByName(String name);
}
