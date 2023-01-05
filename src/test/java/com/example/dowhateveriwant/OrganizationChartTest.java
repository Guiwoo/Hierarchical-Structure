package com.example.dowhateveriwant;

import com.example.dowhateveriwant.entity.OrganizationChart;
import com.example.dowhateveriwant.entity.QOrganizationChart;
import com.example.dowhateveriwant.repository.OrganizationChartRepository;
import com.example.dowhateveriwant.repository.OrganizationChartVer1;
import com.example.dowhateveriwant.repository.PersonRepository;
import com.querydsl.core.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.dowhateveriwant.entity.QOrganizationChart.organizationChart;


@SpringBootTest
@Transactional
public class OrganizationChartTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private OrganizationChartRepository organizationChartRepository;
    @Autowired
    private OrganizationChartVer1 org1;

//    @Test
//    @Commit
//    public void init() throws Exception{
//        String[] s = {"프란","올리","쿠클라","록산나","로건","귀우","신디"};
//        String[] p = {"김","이","박"};
//        List<Person> list = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            list.add(Person.builder()
//                    .firstName(s[i])
//                    .lastName(s[i%3])
//                    .build());
//        }
//        personRepository.saveAll(list);
//    }

    @Test
    @Commit
    void addChart() throws Exception{
        // 101 ~ 107;
        // root
        //    1, 2
        //  3, 4  6
        // 5

        // 1st layer
//        OrganizationChart root =
//                organizationChartRepository.findById(115L).get();
//
//        organizationChartRepository.save(
//                OrganizationChart.builder()
//                        .department("첫번째 레이어 왼쪽")
//                        .organizationChart(root)
//                        .build()
//        );
//        organizationChartRepository.save(
//                OrganizationChart.builder()
//                        .department("첫번째 레이어 오른쪽")
//                        .organizationChart(root)
//                        .build()
//        );
        // 2nd Layer
        OrganizationChart left = organizationChartRepository.findById(116L).get();
        OrganizationChart right = organizationChartRepository.findById(117L).get();

        OrganizationChart 두번째_레이어_왼쪽_왼쪽 = organizationChartRepository.save(
                OrganizationChart.builder()
                        .department("두번째 레이어 왼쪽 왼쪽")
                        .parent(left)
                        .build()
        );
        organizationChartRepository.save(
                OrganizationChart.builder()
                        .department("두번째 레이어 왼쪽 오른쪽")
                        .parent(left)
                        .build()
        );
        organizationChartRepository.save(
                OrganizationChart.builder()
                        .department("두번째 레이어 왼쪽 왼쪽")
                        .parent(right)
                        .build()
        );
//        두번째_레이어_왼쪽_왼쪽
        // third layer
        organizationChartRepository.save(
                OrganizationChart.builder()
                        .department("두번째 레이어 왼쪽 왼쪽 왼쪽")
                        .parent(두번째_레이어_왼쪽_왼쪽)
                        .build()
        );
    }

    @Test
    void move() throws Exception{
        OrganizationChart organizationChart = organizationChartRepository
                .findById(118L).get();
        OrganizationChart organizationChart2 = organizationChartRepository
                .findById(120L).get();
        organizationChart.updateParents(organizationChart2);
    }

    @Test
    void delete () throws Exception{
        OrganizationChart organizationChart = organizationChartRepository
                .findById(115L).get();
        organizationChartRepository.
                delete(organizationChart);
    }

    @Test
    void 조회() throws Exception{
        List<Tuple> specificLayer = org1.getSpecificLayer(116L);
        QOrganizationChart q2 = new QOrganizationChart("q2");
        QOrganizationChart q3 = new QOrganizationChart("q3");
    }
}
