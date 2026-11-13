package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.MealEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {
    List<MealEntry> findAllByUserIdAndDateOrderByTypeAscIdAsc(Long userId, LocalDate date);
    Optional<MealEntry> findByIdAndUserId(Long id, Long userId);
}
