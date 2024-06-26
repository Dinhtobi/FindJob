package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {

    @Query(value = "SELECT * FROM favourite as f where f.candidate_id = :id", nativeQuery = true)
    List<Favourite> findByCandidateId(@Param("id") int id);

    @Query(value = "Select * from favourite where candidate_id = :candidate_id and post_id = :post_id", nativeQuery = true)
    Optional<Favourite> findByCandidateAndPost(@Param("candidate_id") int candidate_id, @Param("post_id") int post_id);
}
