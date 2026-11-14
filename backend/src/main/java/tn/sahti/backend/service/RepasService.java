
package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.sahti.backend.dto.RepasRequest;
import tn.sahti.backend.dto.RepasResponse;
import tn.sahti.backend.entity.Repas;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.RepasRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepasService {

    private final RepasRepository repasRepository;
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
    public RepasResponse ajouterRepas(RepasRequest request) {

        User user = utilisateurConnecte();

        Repas repas = Repas.builder()
                .nom(request.getNom())
                .type(request.getType())
                .calories(request.getCalories())
                .dateEnregistrement(LocalDate.now())
                .user(user)
                .build();

        Repas saved = repasRepository.save(repas);

        return convertirEnResponse(saved);
    }

    public List<RepasResponse> recupererRepasDuJour() {

        User user = utilisateurConnecte();

        return repasRepository
                .findAllByUser_IdAndDateEnregistrementOrderByIdDesc(
                        user.getId(), LocalDate.now())
                .stream()
                .map(this::convertirEnResponse)
                .toList();
    }

    @Transactional
    public void supprimerRepas(Long id) {

        User user = utilisateurConnecte();

        Repas repas = repasRepository
                .findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Repas introuvable"));

        repasRepository.delete(repas);
    }

    private RepasResponse convertirEnResponse(Repas repas) {

        return RepasResponse.builder()
                .id(repas.getId())
                .nom(repas.getNom())
                .type(repas.getType())
                .calories(repas.getCalories())
                .dateEnregistrement(repas.getDateEnregistrement())
                .build();
    }
}
