
package tn.sahti.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiviteRequest {

    @NotBlank(message = "Le type d'activité est obligatoire")
    private String type;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être supérieure à zéro")
    private Integer duree;

    @NotNull(message = "Les calories dépensées sont obligatoires")
    @Min(value = 1, message = "Les calories doivent être positives")
    private Integer caloriesDepensees;
}
