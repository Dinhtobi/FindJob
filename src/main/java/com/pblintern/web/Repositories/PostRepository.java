package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM post as p where p.employer_id = :id  limit 20" ,nativeQuery = true)
    List<Post> getMyJob(@Param("id") int id);
}
