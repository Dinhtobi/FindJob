package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
    Optional<Field> findByName(String name);
}
