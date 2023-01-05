package com.example.dowhateveriwant.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationChart {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String department;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id")
    private OrganizationChart parent;

    public void updateParents(OrganizationChart org){
        this.parent = org;
    }
}
