package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.FieldOfActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<FieldOfActivity, Integer> {
    Optional<FieldOfActivity> findByName(String name);
}
