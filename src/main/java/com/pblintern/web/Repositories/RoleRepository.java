package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Role;
import com.pblintern.web.Enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum role);
}
