package com.pblintern.web.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WorkExperience")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private Date timeStart;
    @Column
    private Date timeEnd;
    @Column(columnDefinition = "TEXT")
    private String position;
    @Column(columnDefinition = "TEXT")
    private String description;
}
