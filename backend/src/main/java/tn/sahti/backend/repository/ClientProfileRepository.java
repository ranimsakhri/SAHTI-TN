package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.ClientProfile;

import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUserId(Long userId);
}
