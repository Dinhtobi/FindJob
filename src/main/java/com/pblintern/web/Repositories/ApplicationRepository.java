package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Application;
import com.pblintern.web.Repositories.projection.ApplicationStatisticChartProjection;
import com.pblintern.web.Repositories.projection.ApplicationSumProjection;
import com.pblintern.web.Repositories.projection.StatisticApplicationProjection;
import com.pblintern.web.Repositories.projection.StatisticPostProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query(value = "SELECT * FROM application as a where a.candidate_id = :id", nativeQuery = true)
    List<Application> findByCandidateId(@Param("id") int id);

    @Query(value = "SELECT * FROM application as a where a.candidate_id = :candidate_id and a.post_id = :post_id" , nativeQuery = true)
    Optional<Application> findByCandidateIdAndPostId(@Param("candidate_id") int candidate_id, @Param("post_id") int post_id);

    @Query(value = "select count(*) as sum from application where post_id in (:ids)", nativeQuery = true)
    ApplicationSumProjection getSumApplicationForRecruiter(@Param("ids") List<Integer> ids);

    @Query(value = "SELECT * FROM application as a where a.post_id = :id order by a.create_at desc", nativeQuery = true)
    List<Application> findByPostId(@Param("id") int id);

    @Query(value = "SELECT a.id , a.status,i.id, i.name FROM application as a inner join (select p.id ,p.name from recruiter as r inner join post as p on r.id = p.recruiter_id where r.id = :id) as i on a.post_id = i.id where a.create_at >= :startDate and a.create_at <= :endDate", nativeQuery = true)
    List<ApplicationStatisticChartProjection> statisticChartDaylyForRecruiter(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("id") int id);

    @Query(value = "SELECT a.id , a.status,i.id, i.name FROM application as a inner join (select p.id ,p.name from recruiter as r inner join post as p on r.id = p.recruiter_id where r.id = :id) as i on a.post_id = i.id where MONTH(a.create_at)  >= MONTH(:startDate) and MONTH(a.create_at)  <= MONTH(:endDate)",nativeQuery = true)
    List<ApplicationStatisticChartProjection> statisticChartMonthlyForRecruiter(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("id") int id);

    @Query(value = "SELECT a.id , a.status,i.id as postId, i.name FROM application as a inner join (select p.id ,p.name from recruiter as r inner join post as p on r.id = p.recruiter_id where r.id = :id) as i on a.post_id = i.id where YEAR(a.create_at) >= YEAR(:startDate) and YEAR(a.create_at) <= YEAR(:endDate)", nativeQuery = true)
    List<ApplicationStatisticChartProjection> statisticChartYearlyForRecruiter(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("id") int id);

    @Query(value = "WITH RECURSIVE date_series AS (" +
            "    SELECT :startDate AS day " +
            "    UNION ALL " +
            "    SELECT DATE_ADD(day, INTERVAL 1 DAY) " +
            "    FROM date_series " +
            "    WHERE day < :endDate " +
            ") " +
            "SELECT ds.day, COALESCE(counts.count, 0) AS count " +
            "FROM date_series ds " +
            "LEFT JOIN ( " +
            "    SELECT DATE(p.create_at) AS day, COUNT(*) AS count " +
            "    FROM application AS p " +
            "    WHERE p.create_at BETWEEN :startDate AND :endDate " +
            "    GROUP BY DATE(p.create_at) " +
            ") counts ON ds.day = counts.day " +
            "ORDER BY ds.day desc",nativeQuery = true)
    List<StatisticApplicationProjection> getStatisticForDayPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "WITH RECURSIVE month_series AS ( " +
            "    SELECT DATE_FORMAT(:startDate, '%Y-%m-01') AS day " +
            "    UNION ALL " +
            "    SELECT DATE_FORMAT(DATE_ADD(day, INTERVAL 1 MONTH), '%Y-%m-01') " +
            "    FROM month_series " +
            "    WHERE day < :endDate " +
            ") " +
            "SELECT MONTH(ms.day) as day , COALESCE(counts.count, 0) AS count " +
            "FROM month_series ms " +
            "LEFT JOIN ( " +
            "    SELECT DATE_FORMAT(p.create_at, '%Y-%m-01') AS day, COUNT(*) AS count " +
            "    FROM application AS p " +
            "    WHERE p.create_at BETWEEN :startDate AND :endDate " +
            "    GROUP BY DATE_FORMAT(p.create_at, '%Y-%m-01') " +
            ") counts ON ms.day = counts.day " +
            "ORDER BY ms.day desc",nativeQuery = true)
    List<StatisticApplicationProjection> getStatisticForMonthPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT YEAR(a.create_at) AS day, COUNT(a.id) AS count FROM application AS a WHERE YEAR(a.create_at) >= :startDate AND YEAR(a.create_at)<= :endDate GROUP BY day",nativeQuery = true)
    List<StatisticApplicationProjection> getStatisticForYearPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
