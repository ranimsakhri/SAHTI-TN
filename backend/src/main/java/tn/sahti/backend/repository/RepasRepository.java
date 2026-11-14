
package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.Repas;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RepasRepository extends JpaRepository<Repas, Long> {

    List<Repas> findAllByUser_IdAndDateEnregistrementOrderByIdDesc(
            Long userId, LocalDate date
    );

    Optional<Repas> findByIdAndUser_Id(Long id, Long userId);

    void deleteByIdAndUser_Id(Long id, Long userId);
}
