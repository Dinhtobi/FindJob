package com.pblintern.web.Entities;

import com.pblintern.web.Enums.NotificationEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private NotificationEnum source;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String message;

    private Boolean isRead;

    private Date createAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
