package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.contest.Contest;
import cfa.fishing.fishing_store_app.entity.contest.ContestRegistration;
import cfa.fishing.fishing_store_app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContestRegistrationRepository extends JpaRepository<ContestRegistration, Long> {
    List<ContestRegistration> findByUser(User user);
    List<ContestRegistration> findByContest(Contest contest);
    Optional<ContestRegistration> findByContestAndUser(Contest contest, User user);
    long countByContest(Contest contest);
}