package tn.sahti.backend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.entity.Profil;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.ProfilRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProfilService {

    private final ProfilRepository profilRepository;
    private final UserRepository userRepository;

    @Transactional
    public Profil enregistrerProfil(
            String email,
            LocalDate dateNaissance,
            String sexe,
            Double taille,
            Double poids,
            String objectif,
            String niveauActivite) {

        // 1. Rechercher le compte connecté
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        // 2. Vérifier si un profil existe déjà
        Profil profil = profilRepository.findByUser(user)
                .orElseGet(() -> Profil.builder().user(user).build());

        // 3. Remplir les informations du profil
        profil.setDateNaissance(dateNaissance);
        profil.setSexe(sexe);
        profil.setTaille(taille);
        profil.setPoids(poids);
        profil.setObjectif(objectif);
        profil.setNiveauActivite(niveauActivite);

        // 4. Enregistrer dans MySQL
        return profilRepository.save(profil);
    }
    //pour recupérer le profil enregistrer
    @Transactional(readOnly = true)
    public Profil recupererProfil(String email){
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "utilisateur introuvable"
                ));
        return profilRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profil non trouvé"
                ));
    }
}
