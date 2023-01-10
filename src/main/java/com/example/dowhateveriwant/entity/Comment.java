package com.example.dowhateveriwant.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    @Column(name ="left_node")
    private int left;
    @Column(name = "right_node")
    private int right;
    private int level;

    public void updateComment(String newComment){
        this.comment = newComment;
    }
}
