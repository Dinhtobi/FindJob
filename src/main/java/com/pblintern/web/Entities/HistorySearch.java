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
@Table(name ="HistorySearch")
public class HistorySearch {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String keyWord;
    @ManyToOne
    @JoinColumn(name = "seeker_id")
    private Seeker seeker;
    @Column
    private Date createAt;

}
