package com.pblintern.web.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pblintern.web.Exceptions.NotFoundException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Table(name = "User")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String fullName;
    @Column
    private String phoneNumber;
    @Column
    private String email;
    @Column
    private String avatar;
    @Column
    private boolean gender;
    @Column
    private Date dateOfBirth;
    @Column
    private Date createAt;
    @Column
    private boolean isNonBlock;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    @JoinTable(name ="user_role", joinColumns =@JoinColumn(name = "user_id" , referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id" , referencedColumnName = "id"))
    private Set<Role> roles;

    public Role getFirstRole(){
        Iterator<Role> roleIterator = roles.iterator();
        if(roleIterator.hasNext()){
            return roles.iterator().next();
        }else{
            throw new NotFoundException("User hasn't Role");
        }
    }
}
