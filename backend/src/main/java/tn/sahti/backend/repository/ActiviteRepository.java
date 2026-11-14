
package tn.sahti.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.Activite;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActiviteRepository extends JpaRepository<Activite, Long> {

    List<Activite> findAllByUser_IdAndDateEnregistrementOrderByIdDesc(
            Long userId, LocalDate date
    );

    Optional<Activite> findByIdAndUser_Id(Long id, Long userId);

    void deleteByIdAndUser_Id(Long id, Long userId);
}
