package com.example.dowhateveriwant;

import com.example.dowhateveriwant.entity.Person;
import com.example.dowhateveriwant.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class OrganizationChartTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Commit
    public void init() throws Exception{
        String[] s = {"프란","올리","쿠클라","록산나","로건","귀우","신디"};
        String[] p = {"김","이","박"};
        List<Person> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(Person.builder()
                    .firstName(s[i])
                    .lastName(s[i%3])
                    .build());
        }
        personRepository.saveAll(list);
    }
}
