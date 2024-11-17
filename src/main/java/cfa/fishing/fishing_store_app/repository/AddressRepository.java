package cfa.fishing.fishing_store_app.repository;

import cfa.fishing.fishing_store_app.entity.user.Address;
import cfa.fishing.fishing_store_app.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserOrderByIsDefaultDescCreatedAtDesc(User user);

    Optional<Address> findByUserAndIsDefaultTrue(User user);

    boolean existsByUserAndIsDefaultTrue(User user);
}