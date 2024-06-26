package com.pblintern.web.Repositories;

import com.pblintern.web.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query(value = "SELECT * FROM notification as n where n.user_id = :user_id  and n.source = 'JOBAPPLICATIONSTATUS' order by n.create_at desc",nativeQuery = true)
    List<Notification> getNotificaitonForCandidate(@Param("user_id") int user_id);

    @Query(value = "SELECT * FROM notification as n where n.user_id = :user_id  and n.source = 'EXPIREDPOSTSTATUS' order by n.create_at desc", nativeQuery = true)
    List<Notification> getNotificaitonForRecuiter(@Param("user_id") int user_id);

    @Query(value = "SELECT * FROM notification as n where n.post_id = :post_id and n.user_id = :recruiter_id " , nativeQuery = true)
    Optional<Notification> getNotificationByPostIdAndRecruiterId(@Param("post_id") int post_id, @Param("recruiter_id") int recruiter_id);

    @Query(value = "SELECT * FROM notification as n where n.post_id = :post_id and n.user_id = :candidate_id and n.source = 'OTHER'" , nativeQuery = true)
    Optional<Notification> getNotificationByPostIdAndCandidateId(@Param("post_id") int post_id, @Param("candidate_id") int recruiter_id);
}
