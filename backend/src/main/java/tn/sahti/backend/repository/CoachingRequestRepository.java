package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.CoachingRequest;
import tn.sahti.backend.entity.Role;

import java.util.List;

public interface CoachingRequestRepository extends JpaRepository<CoachingRequest, Long> {
    List<CoachingRequest> findAllByProfessionalTypeOrderByCreatedAtDesc(Role professionalType);
}
