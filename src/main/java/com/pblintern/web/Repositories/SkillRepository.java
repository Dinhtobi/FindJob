package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Integer> {
    Optional<Skills> findByName(String name);
}
