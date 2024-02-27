package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
