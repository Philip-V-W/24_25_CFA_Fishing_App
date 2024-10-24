package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.contest.Contest;
import cfa.fishing.fishing_store_app.entity.contest.ContestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByStatus(ContestStatus status);

    @Query("SELECT c FROM Contest c WHERE c.startDate > :now ORDER BY c.startDate")
    List<Contest> findUpcomingContests(LocalDateTime now);

    @Query("SELECT c FROM Contest c WHERE c.startDate <= :now AND c.endDate >= :now")
    List<Contest> findOngoingContests(LocalDateTime now);
}