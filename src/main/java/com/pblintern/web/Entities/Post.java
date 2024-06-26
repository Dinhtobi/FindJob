package com.pblintern.web.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


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
    private String requirement;
    @Column
    private Date createAt;
    @Column
    private Date expire;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private int minSalary;
    @Column
    private int maxSalary;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinTable(name ="post_field", joinColumns =@JoinColumn(name = "post_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "field_id" , referencedColumnName = "id"))
    private Set<Field> fields;
    @ManyToOne
    @JoinColumn(name = "recruiter_id")
    private Recruiter recruiter;

}
