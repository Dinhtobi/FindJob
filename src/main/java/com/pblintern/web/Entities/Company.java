package com.pblintern.web.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="Company")
public class Company {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String idCompany;
    @Column
    private String name;
    @Column
    private String webSite;
    @Column(columnDefinition = "TEXT")
    private String location;
    @Column
    private String companySize;
    @Column
    private String companyType;
    @Column
    private String email;
    @Column
    private String phoneNumber;
    @Column
    private String taxCode;
    @Column
    private String businessLicenseImg;
    @Column
    private String logo;
    @Column(columnDefinition = "TEXT")
    private String description;

}
