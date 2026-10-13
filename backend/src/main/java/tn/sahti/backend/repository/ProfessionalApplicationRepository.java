package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.ApplicationStatus;
import tn.sahti.backend.entity.ProfessionalApplication;
import tn.sahti.backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProfessionalApplicationRepository extends JpaRepository<ProfessionalApplication, Long> {
    Optional<ProfessionalApplication> findByUser(User user);
    List<ProfessionalApplication> findAllByOrderBySubmittedAtDesc();
    List<ProfessionalApplication> findByStatusOrderBySubmittedAtDesc(ApplicationStatus status);
}