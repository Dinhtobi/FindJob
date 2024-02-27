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
@Table(name ="Employer")
public class Employer {

    @Id
    private int id;
    @Column
    private String position;
    @Column(nullable = true)
    private int idCompany;

    @OneToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
