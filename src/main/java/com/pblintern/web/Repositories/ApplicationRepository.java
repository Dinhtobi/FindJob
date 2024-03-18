package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query(value = "SELECT * FROM application as a where a.seeker_id = :id", nativeQuery = true)
    List<Application> findBySeekerId(@Param("id") int id);

    @Query(value = "SELECT * FROM application as a where a.seeker_id = :seeker_id and a.post_id = :post_id" , nativeQuery = true)
    Optional<Application> findBySeekerIdAndPostId(@Param("seeker_id") int seeker_id, @Param("post_id") int post_id);
}
