package com.example.dowhateveriwant.repository.pizza;

import com.example.dowhateveriwant.entity.Pizza;
import com.example.dowhateveriwant.entity.PizzaPaths;
import org.hibernate.annotations.Parent;
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

//    @Modifying
//    @Query(value = "insert into pizza_paths (parent,child) " +
//            "select super.parent,sub.child" +
//            " from pizza_paths super" +
//            " cross join pizza_paths sub" +
//            " where super.child = :parentId and sub.parents = :childId",nativeQuery = true)

    @Modifying
    @Query(
            value = "insert into pizza_paths (parents_id,child_id) " +
                    "select sup.parents_id,sub.child_id from pizza_paths sup " +
                    "cross join pizza_paths sub "+
                    "where sup.child_id = :parentsId and sub.parents_id = :childId",
            nativeQuery = true
    )
    void shiftInsertData(@Param("parentsId") Long parentsId,@Param("childId") Long childId);

}
