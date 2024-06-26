package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Post;
import com.pblintern.web.Repositories.projection.CountLocationPostProjection;
import com.pblintern.web.Repositories.projection.StatisticPostLocationProjection;
import com.pblintern.web.Repositories.projection.StatisticPostProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM post as p where p.recruiter_id = :id  " ,nativeQuery = true)
    List<Post> getMyJob(@Param("id") int id);
    @Query(value = "SELECT * FROM post as p where p.company_id = :id", nativeQuery = true)
    List<Post> getJobOfCompany(@Param("id") int id);

    @Query(value = "SELECT p.id from post as p where p.expire <= :now " , nativeQuery = true )
    List<Integer> getIds(@Param("now") @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss") Date now);

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
            "    FROM post AS p " +
            "    WHERE p.create_at BETWEEN :startDate AND :endDate " +
            "    GROUP BY DATE(p.create_at) " +
            ") counts ON ds.day = counts.day " +
            "ORDER BY ds.day desc",nativeQuery = true)
    List<StatisticPostProjection> getStatisticForDayPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "WITH RECURSIVE month_series AS ( " +
            "    SELECT DATE_FORMAT(:startDate, '%Y-%m-01') AS day " +
            "    UNION ALL " +
            "    SELECT DATE_FORMAT(DATE_ADD(day, INTERVAL 1 MONTH), '%Y-%m-01') " +
            "    FROM month_series " +
            "    WHERE day < :endDate " +
            ") " +
            "SELECT MONTH(ms.day) as day, COALESCE(counts.count, 0) AS count " +
            "FROM month_series ms " +
            "LEFT JOIN ( " +
            "    SELECT DATE_FORMAT(p.create_at, '%Y-%m-01') AS day, COUNT(*) AS count " +
            "    FROM post AS p " +
            "    WHERE p.create_at BETWEEN :startDate AND :endDate " +
            "    GROUP BY DATE_FORMAT(p.create_at, '%Y-%m-01') " +
            ") counts ON ms.day = counts.day " +
            "ORDER BY ms.day desc ",nativeQuery = true)
    List<StatisticPostProjection> getStatisticForMonthPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "SELECT YEAR(p.create_at) AS day, COUNT(p.id) AS count FROM post AS p WHERE YEAR(p.create_at) >= :startDate AND YEAR(p.create_at) <= :endDate GROUP BY day",nativeQuery = true)
    List<StatisticPostProjection> getStatisticForYearPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select count(*) as count from post as p inner join company as a on a.id = p.company_id WHERE a.location like :location  AND p.create_at >= :startDate AND p.create_at <= :endDate ",nativeQuery = true)
    CountLocationPostProjection getStatisticLocationForDayPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("location") String location );

    @Query(value = "select count(*) as count from post as p inner join company as a on a.id = p.company_id WHERE a.location like :location  AND MONTH(p.create_at) >= :startDate AND MONTH(p.create_at) <= :endDate ",nativeQuery = true)
    CountLocationPostProjection getStatisticLocationForMonthPost(@Param("startDate") int startDate, @Param("endDate") int endDate, @Param("location") String location );

    @Query(value = "select count(*) as count from post as p inner join company as a on a.id = p.company_id WHERE a.location like :location  AND YEAR(p.create_at) >= :startDate AND YEAR(p.create_at) <= :endDate ",nativeQuery = true)
    CountLocationPostProjection getStatisticLocationForYearPost(@Param("startDate") int startDate, @Param("endDate") int endDate, @Param("location") String location );

    @Query(value = "select count(*) as count from post as p inner join company as a on a.id = p.company_id WHERE a.location not like '%Đà Nẵng%' And a.location not like '%HCM%' And a.location not like '%Hà Nội%'  AND p.create_at >= :startDate AND p.create_at <= :endDate ",nativeQuery = true)
    CountLocationPostProjection getStatisticLocationOtherForDayPost(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value = "select count(*) as count from post as p inner join company as a on a.id = p.company_id WHERE a.location not like '%Đà Nẵng%' And a.location not like '%HCM%' And a.location not like '%Hà Nội%' AND MONTH(p.create_at) >= :startDate AND MONTH(p.create_at) <= :endDate ",nativeQuery = true)
    CountLocationPostProjection getStatisticLocationOtherForMonthPost(@Param("startDate") int startDate, @Param("endDate") int endDate);

    @Query(value = "select count(*) as count from post as p inner join company as a on a.id = p.company_id WHERE a.location not like '%Đà Nẵng%' And a.location not like '%HCM%' And a.location not like '%Hà Nội%' AND YEAR(p.create_at) >= :startDate AND YEAR(p.create_at) <= :endDate ",nativeQuery = true)
    CountLocationPostProjection getStatisticLocationOtherForYearPost(@Param("startDate") int startDate, @Param("endDate") int endDate );

}
