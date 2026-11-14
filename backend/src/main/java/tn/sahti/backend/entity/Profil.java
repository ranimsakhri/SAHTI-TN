package tn.sahti.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "profils")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private LocalDate dateNaissance;

    private String sexe;

    private Double taille;

    private Double poids;

    private String objectif;

    private String niveauActivite;
}
