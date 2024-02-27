package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.HistorySearch;
import com.pblintern.web.Repositories.projection.HistorySearchProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorySearchRepository extends JpaRepository<HistorySearch, Integer> {
    @Query(value = "SELECT h.key_word as keyWord FROM history_search as h inner join user as u on u.id = h.seeker_id" +
            " where u.email = :email order by h.create_at desc limit 5",nativeQuery = true)
    List<HistorySearchProjection> findFiveDESC(@Param("email") String email);
}
