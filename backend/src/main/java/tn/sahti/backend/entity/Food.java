package tn.sahti.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 50)
    private String origin;

    @Column(nullable = false, length = 80)
    private String serving;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal caloriesPer100g;

    @Column(precision = 6, scale = 2)
    private BigDecimal proteinPer100g;

    @Column(precision = 6, scale = 2)
    private BigDecimal carbohydratesPer100g;

    @Column(precision = 6, scale = 2)
    private BigDecimal fatPer100g;
}
