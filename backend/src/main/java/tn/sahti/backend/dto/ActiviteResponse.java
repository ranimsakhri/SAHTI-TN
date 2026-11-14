
package tn.sahti.backend.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActiviteResponse {

    private Long id;
    private String type;
    private Integer duree;
    private Integer caloriesDepensees;
    private LocalDate dateEnregistrement;
}
