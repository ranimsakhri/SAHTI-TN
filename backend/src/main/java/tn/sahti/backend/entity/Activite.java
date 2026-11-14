package tn.sahti.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "activites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer duree;

    @Column(nullable = false)
    private Integer caloriesDepensees;

    @Column(nullable = false)
    private LocalDate dateEnregistrement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (dateEnregistrement == null) {
            dateEnregistrement = LocalDate.now();
        }
    }
}
