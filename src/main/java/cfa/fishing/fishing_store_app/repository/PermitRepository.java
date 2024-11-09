package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.permit.FishingPermit;
import cfa.fishing.fishing_store_app.entity.permit.PermitStatus;
import cfa.fishing.fishing_store_app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PermitRepository extends JpaRepository<FishingPermit, Long> {
    List<FishingPermit> findByUser(User user);
    List<FishingPermit> findByUserOrderByCreatedAtDesc(User user);
    List<FishingPermit> findByStatus(PermitStatus status);
    Optional<FishingPermit> findByPermitNumber(String permitNumber);

    @Query("SELECT p FROM FishingPermit p WHERE p.user = ?1 AND p.status = 'APPROVED' " +
            "AND p.startDate <= ?2 AND p.endDate >= ?2")
    List<FishingPermit> findActivePermits(User user, LocalDate date);

    @Query("SELECT COUNT(p) FROM FishingPermit p WHERE p.status = 'PENDING'")
    long countPendingPermits();
}