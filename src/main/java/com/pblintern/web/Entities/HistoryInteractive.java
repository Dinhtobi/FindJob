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
@Table(name ="HistoryInteractive")
public class HistoryInteractive {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int candidate_id;

    private int post_id;

    private Date createAt;
}
