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
    @Column
    private String name;
    @Column
    private String level;
    @Column
    private String experience;
    @Column
    private String requirements;
    @Column
    private Date createAt;
    @Column
    private Date expire;
    @Column
    private String description;
    @Column
    private int salary;
    @Column
    private int views;
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToOne
    @JoinColumn(name = "field_id")
    private FieldOfActivity fieldOfActivity;
    @OneToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

}
