package com.pblintern.web.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Candidate")
public class Candidate {

    @Id
    private int id;
    @Column
    private String address;
    @Column
    private String cvUrl;
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinTable(name ="candidate_skill", joinColumns =@JoinColumn(name = "candidate_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id" , referencedColumnName = "id"))
    private Set<Skills> skills;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinTable(name ="candidate_jobPost", joinColumns =@JoinColumn(name = "candidate_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "jobPost_id" , referencedColumnName = "id"))
    private Set<Post> jobPosts;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

}
