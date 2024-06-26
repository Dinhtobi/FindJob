package com.pblintern.web.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Recruiter")
public class Recruiter {

    @Id
    private int id;
    @Column
    private String position;
    @Column
    private String verification_code;
    @Column
    private Boolean enable;

    @ManyToOne
    @JoinColumn(name = "company_id" , nullable=false)
    private Company company;

    @OneToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
