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
@Table(name ="HistoryInteractive")
public class HistoryInteractive {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "seeker_id")
    private Seeker seeker;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post jobPost;

    private Date createAt;
}
