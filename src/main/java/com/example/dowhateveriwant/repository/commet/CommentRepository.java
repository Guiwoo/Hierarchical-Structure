package com.example.dowhateveriwant.repository.commet;

import com.example.dowhateveriwant.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByComment(String comment);
}
