package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByIdCompany(String idCompany);

    Optional<Company> findByName(String name);
}
