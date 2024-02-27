package com.pblintern.web.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "seeker_id")
    private Seeker seeker;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post jobPost;

    @Column
    private String introduct;
}
