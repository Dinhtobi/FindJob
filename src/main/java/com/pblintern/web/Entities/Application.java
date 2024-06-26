package com.pblintern.web.Entities;

import com.pblintern.web.Enums.ApplicationEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Application")
public class    Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int candidate_id;
    private int  post_id;
    @Column
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    private ApplicationEnum status;

    private Date createAt;
}
