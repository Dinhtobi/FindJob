package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Integer> {

    @Query(value = "SELECT e.* FROM recruiter as e inner join user as u on e.id = u.id where u.email = :email", nativeQuery = true)
    Optional<Recruiter> findByEmail(@Param("email") String email);
}
