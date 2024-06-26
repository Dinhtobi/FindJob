package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Company;
import com.pblintern.web.Repositories.projection.CompanyDetailProjection;
import com.pblintern.web.Repositories.projection.CompanyProjection;
import com.pblintern.web.Repositories.projection.CompanySelectProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByIdCompany(String idCompany);

    Optional<Company> findByName(String name);

    Optional<Company> findByEmail(String email);

    @Query(value = "SELECT c.id, c.name, c.logo AS logo from company as c inner join post as p on p.company_id = c.id group by c.id, c.name order by count(p.id) desc limit :size" , nativeQuery = true)
    List<CompanyProjection> getTopCompany(@Param("size") int size);

    @Query(value = "SELECT c.id, c.name, c.logo, c.company_size as size, c.company_type as type, c.location from company as c ", nativeQuery = true)
    List<CompanyDetailProjection> getCompanies();

    @Query(value = "SELECT  c.id, c.name, c.logo, c.company_size as size, c.company_type as type, c.location from company as c inner join post as p on p.company_id = c.id group by c.id, c.name order by count(p.id) desc limit :size ", nativeQuery = true)
    List<CompanyDetailProjection> getTopCompanyDetail(@Param("size") int size);

    @Query(value = "SELECT c.id, c.name, c.logo from company as c ", nativeQuery = true)
    List<CompanySelectProjection> getCompanySelect();

}
