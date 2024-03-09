package com.pblintern.web.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "TEXT")
    private String name;
    @Column
    private String level;
    @Column(columnDefinition = "TEXT")
    private String experience;
    @Column(columnDefinition = "TEXT")
    private String requirements;
    @Column
    private Date createAt;
    @Column
    private Date expire;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private String salary;
    @Column
    private int views;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private FieldOfActivity fieldOfActivity;
    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

}
