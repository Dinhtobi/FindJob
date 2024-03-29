package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.HistoryInteractive;
import com.pblintern.web.Repositories.projection.HistoryInteractiveProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryInteractiveRepository extends JpaRepository<HistoryInteractive,Integer> {
    @Query(value = "SELECT h.post_id as IdPost FROM history_interactive as h inner join user as u on u.id = h.seeker_id" +
            " where u.email = :email order by h.create_at desc limit 5" , nativeQuery = true)
    List<HistoryInteractiveProjection> findFiveDESC(@Param("email") String email);
}
