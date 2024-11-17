package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.contest.Contest;
import cfa.fishing.fishing_store_app.entity.contest.ContestRegistration;
import cfa.fishing.fishing_store_app.entity.contest.ContestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByStatus(ContestStatus status);

    @Query("SELECT c FROM Contest c WHERE c.startDate > :now ORDER BY c.startDate")
    List<Contest> findUpcomingContests(LocalDateTime now);

    @Query("SELECT c FROM Contest c WHERE c.startDate <= :now AND c.endDate >= :now")
    List<Contest> findOngoingContests(LocalDateTime now);

    @Query("SELECT COUNT(c) FROM Contest c WHERE c.startDate > ?1")
    long countUpcomingContests(LocalDateTime date);

    @Query("SELECT cr FROM ContestRegistration cr WHERE cr.user.id = :userId AND cr.contest.startDate > CURRENT_DATE")
    List<ContestRegistration> findUserUpcomingContests(@Param("userId") Long userId);


}