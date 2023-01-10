package com.example.dowhateveriwant.entity;

import com.example.dowhateveriwant.repository.commet.CommentQuery;
import com.example.dowhateveriwant.repository.commet.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@SpringBootTest
@Transactional
class CommentTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentQuery commentQuery;
    @Autowired
    private EntityManager em;

    @BeforeEach
    private void init(){
        //insert root
        Comment root = Comment.builder()
                .left(1)
                .right(2)
                .level(0)
                .comment("3 대 500 인 사람 댓글 달아봐")
                .build();

        Comment save = commentRepository.save(root);
        //insert 1st Layer
        insertCommentAndFlushClear(save,"3대 500 아래는 없어요 ?");
        insertCommentAndFlushClear(save,"3대 520 언더아머 회원임");

//        //insert 2nd Layer
        Comment comment1 = commentRepository.findByComment("3대 500 아래는 없어요 ?").get();
        insertCommentAndFlushClear(comment1,"3대 490 ㅠㅠ");
        insertCommentAndFlushClear(comment1, "300 인대요 ? 사람이세요?");

//        //insert 2nd Layer
        Comment comment2 = commentRepository.findByComment("3대 520 언더아머 회원임").get();
        insertCommentAndFlushClear(comment2,"스벤데 몇이고");
        insertCommentAndFlushClear(comment2,"3대 660");

//        //insert 3rd Layer
        Comment comment3 = commentRepository.findByComment("스벤데 몇이고").get();
        insertCommentAndFlushClear(comment3,"구라네 10 이 비는데 ?");
    }

    @AfterEach
    public void afterInit(){
        em.flush();
        em.clear();
    }

    private void insertCommentAndFlushClear(Comment parent,String comment){
        commentQuery.insertComment(parent,comment);
        em.flush();
        em.clear();
    }

    @Test
    void insertTest() throws Exception{
        List<Comment> all = commentRepository.findAll();
        for (Comment comment1 : all) {
            System.out.println(comment1);
        }

        Comment comment = commentRepository.findByComment("3 대 500 인 사람 댓글 달아봐").get();
        Assertions.assertThat(comment.getLeft()).isEqualTo(1);
        Assertions.assertThat(comment.getRight()).isEqualTo(16);
    }

    @Test
    public void insertBetween() throws Exception{
        Comment comment = commentRepository.findByComment("3 대 500 인 사람 댓글 달아봐").get();
        Comment tail = commentRepository.findByComment("3대 520 언더아머 회원임").get();
        commentQuery.insertBetween(comment,tail,"3대 1억");
        em.clear();
        em.flush();

        List<Comment> all = commentRepository.findAll();
        for (Comment comment1 : all) {
            System.out.println(comment1);
        }

        comment = commentRepository.findByComment("3 대 500 인 사람 댓글 달아봐").get();
        Assertions.assertThat(comment.getLeft()).isEqualTo(1);
        Assertions.assertThat(comment.getRight()).isEqualTo(18);
    }

    @Test
    public void updateComment() throws Exception{
        String updateOne = "업데이트 된 커멘트";
        Comment comment = commentRepository.findByComment("3 대 500 인 사람 댓글 달아봐").get();
        comment.updateComment(updateOne);

        em.flush();
        em.clear();

        comment = commentRepository.findByComment(updateOne).get();
        Assertions.assertThat(comment.getComment()).isEqualTo(updateOne);
    }

    @Test
    void deleteComment() throws Exception{
        Comment comment = commentRepository.findByComment("3대 500 아래는 없어요 ?").get();

        commentQuery.deleteComment(comment);

        em.flush();
        em.clear();

        List<Comment> all = commentRepository.findAll();
        for (Comment comment1 : all) {
            System.out.println(comment1);
        }
        Comment comment1 = commentRepository.findByComment("3 대 500 인 사람 댓글 달아봐").get();
        Assertions.assertThat(comment1.getRight()).isEqualTo(14);
    }

    @Test
    void selectSpecificLayer() throws Exception{
        for (int i = 0; i < 3; i++) {
            List<Comment> byLayer = commentQuery.findByLayer(i);
            System.out.println("========= " + i + " ============");
            for (Comment comment : byLayer) {
                System.out.println(comment);
            }
        }
    }

    @Test
    void getAllChildByComment() throws Exception{
        //2번째 레이어 2개의 댓글 에 한개의 대댓글
        Comment comment = commentRepository.findByComment("3대 520 언더아머 회원임").get();
        List<Comment> allCommentFromComment = commentQuery.getAllCommentFromComment(comment);
        Assertions.assertThat(allCommentFromComment.size()).isEqualTo(3);
        for (Comment comment1 : allCommentFromComment) {
            System.out.println(comment1);
        }
    }
}