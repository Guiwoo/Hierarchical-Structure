package com.example.dowhateveriwant.repository;

import com.example.dowhateveriwant.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {
}
