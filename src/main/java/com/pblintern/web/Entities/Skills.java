package com.pblintern.web.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Skills")
public class Skills {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;

    @ManyToMany(mappedBy = "skills")
    private Set<Seeker> seekers;

}
