
package tn.sahti.backend.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepasResponse {

    private Long id;
    private String nom;
    private String type;
    private Integer calories;
    private LocalDate dateEnregistrement;
}
