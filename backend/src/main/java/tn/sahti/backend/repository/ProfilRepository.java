package tn.sahti.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.sahti.backend.entity.Profil;
import tn.sahti.backend.entity.User;

import java.util.Optional;

public interface ProfilRepository extends JpaRepository<Profil, Long> {

    Optional<Profil> findByUser(User user);

    boolean existsByUser(User user);
}

