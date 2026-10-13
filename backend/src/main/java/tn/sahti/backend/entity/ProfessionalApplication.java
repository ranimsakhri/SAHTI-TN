package tn.sahti.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "professional_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessionalApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role type;

    @Column(nullable = false)
    private String specialite;

    @Column(length = 1000)
    private String bio;

    @Column(nullable = false)
    private String cvPath;

    @Column(nullable = false)
    private String lettrePath;

    @Column(nullable = false)
    private String diplomePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private String motifRefus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }
}