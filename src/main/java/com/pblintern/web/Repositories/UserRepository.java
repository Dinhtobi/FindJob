package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    @Query(value = "Select u.id from user as u inner join recruiter as r on u.id = r.id where u.create_at <= :now and r.enable = false", nativeQuery = true)
    List<Integer> getIds(@Param("now") @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")Date now);
}
