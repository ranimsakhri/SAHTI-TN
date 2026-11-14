package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.ActivityEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityEntryRepository extends JpaRepository<ActivityEntry, Long> {
    List<ActivityEntry> findAllByUserIdAndDateOrderByIdDesc(Long userId, LocalDate date);
    Optional<ActivityEntry> findByIdAndUserId(Long id, Long userId);
}
