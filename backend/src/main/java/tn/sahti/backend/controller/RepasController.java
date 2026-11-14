
package tn.sahti.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.sahti.backend.dto.RepasRequest;
import tn.sahti.backend.dto.RepasResponse;
import tn.sahti.backend.service.RepasService;

import java.util.List;

@RestController
@RequestMapping("/api/repas")
@RequiredArgsConstructor
public class RepasController {

    private final RepasService repasService;

    // Ajouter un repas
    @PostMapping
    public ResponseEntity<RepasResponse> ajouterRepas(
            @Valid @RequestBody RepasRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repasService.ajouterRepas(request));
    }

    // Récupérer les repas du jour
    @GetMapping
    public ResponseEntity<List<RepasResponse>> recupererRepas() {

        return ResponseEntity.ok(
                repasService.recupererRepasDuJour()
        );
    }

    // Supprimer un repas
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerRepas(
            @PathVariable Long id) {

        repasService.supprimerRepas(id);

        return ResponseEntity.noContent().build();
    }
}
