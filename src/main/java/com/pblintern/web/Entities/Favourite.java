package com.pblintern.web.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Favourite")
public class Favourite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    private int candidate_id;

    private int post_id;

    private Date createAt;
}
