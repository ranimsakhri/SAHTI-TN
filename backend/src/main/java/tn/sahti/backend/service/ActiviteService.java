
package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.sahti.backend.dto.ActiviteRequest;
import tn.sahti.backend.dto.ActiviteResponse;
import tn.sahti.backend.entity.Activite;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.ActiviteRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActiviteService {

    private final ActiviteRepository activiteRepository;
    private final UserRepository userRepository;

    private User utilisateurConnecte() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));
    }

    @Transactional
    public ActiviteResponse ajouterActivite(ActiviteRequest request) {

        User user = utilisateurConnecte();

        Activite activite = Activite.builder()
                .type(request.getType())
                .duree(request.getDuree())
                .caloriesDepensees(request.getCaloriesDepensees())
                .dateEnregistrement(LocalDate.now())
                .user(user)
                .build();

        Activite saved = activiteRepository.save(activite);

        return convertirEnResponse(saved);
    }

    public List<ActiviteResponse> recupererActivitesDuJour() {

        User user = utilisateurConnecte();

        return activiteRepository
                .findAllByUser_IdAndDateEnregistrementOrderByIdDesc(
                        user.getId(), LocalDate.now())
                .stream()
                .map(this::convertirEnResponse)
                .toList();
    }

    @Transactional
    public void supprimerActivite(Long id) {

        User user = utilisateurConnecte();

        Activite activite = activiteRepository
                .findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Activité introuvable"));

        activiteRepository.delete(activite);
    }

    private ActiviteResponse convertirEnResponse(Activite activite) {

        return ActiviteResponse.builder()
                .id(activite.getId())
                .type(activite.getType())
                .duree(activite.getDuree())
                .caloriesDepensees(activite.getCaloriesDepensees())
                .dateEnregistrement(activite.getDateEnregistrement())
                .build();
    }
}
