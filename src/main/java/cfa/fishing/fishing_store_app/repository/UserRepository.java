package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER'")
    long countCustomers();

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER' AND u.createdAt BETWEEN ?1 AND ?2")
    long countCustomersJoinedBetween(LocalDateTime start, LocalDateTime end);
}