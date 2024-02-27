package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
