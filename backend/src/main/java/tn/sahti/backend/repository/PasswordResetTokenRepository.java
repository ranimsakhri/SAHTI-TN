package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}