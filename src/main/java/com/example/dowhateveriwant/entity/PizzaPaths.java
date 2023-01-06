package com.example.dowhateveriwant.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"parents_id", "child_id"})
})
@ToString
public class PizzaPaths {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parents_id")
    private Pizza parents;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Pizza child;
}
