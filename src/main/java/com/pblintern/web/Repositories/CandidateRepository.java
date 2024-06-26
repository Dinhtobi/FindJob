package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    @Query(value = "SELECT s.* FROM candidate as s inner join user as u on s.id = u.id where u.email = :email", nativeQuery = true)
    Optional<Candidate> findByEmail(@Param("email") String email);
}
