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
@Table(name ="Seeker")
public class Seeker {

    @Id
    private int id;
    @Column
    private String address;
    @Column
    private String cvImg;
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private User user;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinTable(name ="seeker_skill", joinColumns =@JoinColumn(name = "seeker_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id" , referencedColumnName = "id"))
    private Set<Skills> skills;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name  ="seeker_workExperience", joinColumns =@JoinColumn(name = "seeker_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "workExperience_id" , referencedColumnName = "id"))
    private Set<WorkExperience> workExperiences;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinTable(name ="seeker_jobPost", joinColumns =@JoinColumn(name = "seeker_id" , referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "jobPost_id" , referencedColumnName = "id"))
    private Set<Post> jobPosts;

}
