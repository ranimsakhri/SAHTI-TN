
package tn.sahti.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.sahti.backend.dto.ActiviteRequest;
import tn.sahti.backend.dto.ActiviteResponse;
import tn.sahti.backend.service.ActiviteService;

import java.util.List;

@RestController
@RequestMapping("/api/activites")
@RequiredArgsConstructor
public class ActiviteController {

    private final ActiviteService activiteService;

    // Ajouter une activité
    @PostMapping
    public ResponseEntity<ActiviteResponse> ajouterActivite(
            @Valid @RequestBody ActiviteRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(activiteService.ajouterActivite(request));
    }

    // Récupérer les activités du jour
    @GetMapping
    public ResponseEntity<List<ActiviteResponse>> recupererActivites() {

        return ResponseEntity.ok(
                activiteService.recupererActivitesDuJour()
        );
    }

    // Supprimer une activité
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerActivite(
            @PathVariable Long id) {

        activiteService.supprimerActivite(id);

        return ResponseEntity.noContent().build();
    }
}
