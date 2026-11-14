
package tn.sahti.backend.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.entity.Profil;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.service.ProfilService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfilService profilService;

    // Informations du compte connecté
    @GetMapping
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
                "email", auth.getName(),
                "roles", auth.getAuthorities().toString()
        );
    }

    // Enregistrer ou modifier le profil
    @PostMapping
    public ResponseEntity<Map<String, Object>> enregistrerProfil(
            @RequestBody ProfilRequest request,
            Authentication auth) {

        Profil profil = profilService.enregistrerProfil(
                auth.getName(),
                request.getDateNaissance(),
                request.getSexe(),
                request.getTaille(),
                request.getPoids(),
                request.getObjectif(),
                request.getNiveauActivite()
        );

        Map<String, Object> response = Map.of(
                "message", "Profil enregistré avec succès",
                "id", profil.getId(),
                "dateNaissance", profil.getDateNaissance().toString(),
                "sexe", profil.getSexe(),
                "taille", profil.getTaille(),
                "poids", profil.getPoids(),
                "objectif", profil.getObjectif(),
                "niveauActivite", profil.getNiveauActivite()
        );

        return ResponseEntity.ok(response);
    }


    public static class ProfilRequest {

        private LocalDate dateNaissance;
        private String sexe;
        private Double taille;
        private Double poids;
        private String objectif;
        private String niveauActivite;

        public LocalDate getDateNaissance() {
            return dateNaissance;
        }

        public void setDateNaissance(LocalDate dateNaissance) {
            this.dateNaissance = dateNaissance;
        }

        public String getSexe() {
            return sexe;
        }

        public void setSexe(String sexe) {
            this.sexe = sexe;
        }

        public Double getTaille() {
            return taille;
        }

        public void setTaille(Double taille) {
            this.taille = taille;
        }

        public String getObjectif() {
            return objectif;
        }

        public void setObjectif(String objectif) {
            this.objectif = objectif;
        }

        public Double getPoids() {
            return poids;
        }

        public void setPoids(Double poids) {
            this.poids = poids;
        }

        public String getNiveauActivite() {
            return niveauActivite;
        }

        public void setNiveauActivite(String niveauActivite) {
            this.niveauActivite = niveauActivite;
        }
    }
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> recupererProfil(
            Authentication auth) {

        Profil profil = profilService.recupererProfil(auth.getName());

        Map<String, Object> response = Map.of(
                "id", profil.getId(),
                "dateNaissance", profil.getDateNaissance().toString(),
                "sexe", profil.getSexe(),
                "taille", profil.getTaille(),
                "poids", profil.getPoids(),
                "objectif", profil.getObjectif(),
                "niveauActivite", profil.getNiveauActivite()
        );

        return ResponseEntity.ok(response);
    }
}
