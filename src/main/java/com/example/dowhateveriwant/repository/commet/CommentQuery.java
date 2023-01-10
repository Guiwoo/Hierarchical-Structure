package com.example.dowhateveriwant.repository.commet;

import com.example.dowhateveriwant.entity.Comment;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.dowhateveriwant.entity.QComment.comment1;


@Repository
public class CommentQuery {
    private JPAQueryFactory queryFactory;
    private CommentRepository commentRepository;

    public CommentQuery(EntityManager em,CommentRepository cm) {
        this.queryFactory = new JPAQueryFactory(em);
        this.commentRepository = cm;
    }

    public void insertComment(Comment parents, String comment){

        insertUpdateLeftRight(parents.getRight());
        // 데이터 인설트
        commentRepository.save(Comment.builder()
                        .left(parents.getRight())
                        .right(parents.getRight() + 1)
                        .level(parents.getLevel() + 1)
                        .comment(comment).build()
        );
    }
    public void insertUpdateLeftRight(int target){
        queryFactory.update(comment1)
                .set(comment1.left, getLeftCaseBuilder(target))
                .set(comment1.right,comment1.right.add(2))
                .where(comment1.right.goe(target))
                .execute();
    }
    public void insertBetween(Comment head,Comment tail,String comment){
        queryFactory.update(comment1)
                .set(comment1.left, comment1.left.add(1))
                .set(comment1.right, comment1.right.add(1))
                .set(comment1.level, comment1.level.add(1))
                .where(comment1.left.goe(tail.getLeft()),
                        comment1.right.loe(tail.getRight()))
                .execute();

        queryFactory.update(comment1)
                .set(comment1.left,getLeftCaseBuilderWithoutRoot(tail.getLeft()))
                .set(comment1.right,comment1.right.add(2))
                .where(comment1.right.gt(tail.getRight()),
                        comment1.ne(tail))
                .execute();

        commentRepository.save(Comment.builder()
                .left(head.getLeft()+1)
                .right(tail.getRight()+2)
                .level(head.getLevel() + 1)
                .comment(comment).build());
    }

    public List<Comment> findByLayer(int level){
        return queryFactory.selectFrom(comment1)
                .where(comment1.level.eq(level))
                .fetch();
    }

    public List<Comment> getAllCommentFromComment(Comment comment){
        return queryFactory.selectFrom(comment1)
                .where(comment1.left.gt(comment.getLeft()),
                        comment1.right.lt(comment.getRight()))
                .fetch();
    }

    public void deleteComment(Comment comment){
        queryFactory.update(comment1)
                .set(comment1.left, subTractWithoutLeftRoot(comment.getLeft()))
                .set(comment1.right, comment1.right.subtract(2))
                .where(comment1.right.gt(comment.getRight()))
                .execute();

        queryFactory.update(comment1)
                .set(comment1.left, comment1.left.subtract(1))
                .set(comment1.right, comment1.right.subtract(1))
                .set(comment1.level, comment1.level.subtract(1))
                .where(comment1.left.gt(comment.getLeft()),
                        comment1.right.lt(comment.getRight()))
                .execute();


        queryFactory.delete(comment1).where(comment1.eq(comment)).execute();
    }
    private NumberExpression<Integer> subTractWithoutLeftRoot(int target) {
        return new CaseBuilder()
                .when(comment1.left.gt(target))
                .then(comment1.left.subtract(2))
                .otherwise(comment1.left);
    }

    private NumberExpression<Integer> getLeftCaseBuilderWithoutRoot(int target) {
        return new CaseBuilder()
                .when(comment1.left.gt(target))
                .then(comment1.left.add(2))
                .otherwise(comment1.left);
    }

    private NumberExpression<Integer> getLeftCaseBuilder(int target) {
        return new CaseBuilder()
                .when(comment1.left.goe(target))
                .then(comment1.left.add(2))
                .otherwise(comment1.left);
    }
}
