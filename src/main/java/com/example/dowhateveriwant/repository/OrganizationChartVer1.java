package com.example.dowhateveriwant.repository;

import com.example.dowhateveriwant.entity.OrganizationChart;
import com.example.dowhateveriwant.entity.QOrganizationChart;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dowhateveriwant.entity.QOrganizationChart.organizationChart;


@Repository
public class OrganizationChartVer1 {

    private JPAQueryFactory queryFactory;

    public OrganizationChartVer1(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Tuple> getSpecificLayer(Long id){
        QOrganizationChart q2 = new QOrganizationChart("q2");
        QOrganizationChart q3 = new QOrganizationChart("q3");
        List<OrganizationChart> fetch = queryFactory.select(q3)
                .from(q2)
                .leftJoin(q2.parent, organizationChart)
                .leftJoin(organizationChart.parent, q3)
                .where(organizationChart.id.eq(id))
                .fetch();

        for (OrganizationChart chart : fetch) {
            System.out.println(chart.getId());
        }

        return null;
    }
}
