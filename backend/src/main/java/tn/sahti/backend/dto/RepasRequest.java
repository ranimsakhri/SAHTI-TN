
package tn.sahti.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepasRequest {

    @NotBlank(message = "Le nom du repas est obligatoire")
    @Size(max = 100)
    private String nom;

    @NotBlank(message = "Le type du repas est obligatoire")
    private String type;

    @NotNull(message = "Les calories sont obligatoires")
    @Min(value = 1, message = "Les calories doivent être positives")
    private Integer calories;
}
