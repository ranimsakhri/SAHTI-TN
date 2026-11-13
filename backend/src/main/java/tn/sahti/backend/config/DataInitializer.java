package tn.sahti.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.sahti.backend.entity.Role;
import tn.sahti.backend.entity.Food;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.entity.UserStatus;
import tn.sahti.backend.repository.FoodRepository;
import tn.sahti.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${sahti.admin.email}")
    private String adminEmail;

    @Value("${sahti.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail(adminEmail)) {
            userRepository.save(User.builder()
                    .nom("Admin")
                    .prenom("SA7TI")
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(Role.ADMIN)
                    .status(UserStatus.ACTIF)
                    .build());
        }
        seedFoods();
    }

    private void seedFoods() {
        List<FoodSeed> foods = List.of(
                new FoodSeed("Couscous aux légumes", "Plat", 145, 5.0, 25.0, 2.5),
                new FoodSeed("Lablabi", "Plat", 125, 6.5, 18.0, 3.0),
                new FoodSeed("Ojja aux œufs", "Plat", 118, 7.0, 8.0, 6.0),
                new FoodSeed("Brik au thon", "Entrée", 225, 11.0, 22.0, 10.0),
                new FoodSeed("Slata mechouia", "Entrée", 75, 2.0, 8.0, 4.0),
                new FoodSeed("Chorba frik", "Entrée", 82, 4.0, 12.0, 2.0),
                new FoodSeed("Salade tunisienne", "Entrée", 95, 2.0, 9.0, 6.0),
                new FoodSeed("Poulet grillé", "Protéine", 165, 31.0, 0.0, 3.6),
                new FoodSeed("Œuf", "Protéine", 143, 13.0, 1.0, 10.0),
                new FoodSeed("Pois chiches cuits", "Légumineuse", 164, 8.9, 27.0, 2.6),
                new FoodSeed("Lentilles cuites", "Légumineuse", 116, 9.0, 20.0, 0.4),
                new FoodSeed("Semoule cuite", "Céréale", 112, 3.8, 23.0, 0.2),
                new FoodSeed("Pain complet", "Céréale", 247, 13.0, 41.0, 4.2),
                new FoodSeed("Dattes", "Fruit", 282, 2.5, 75.0, 0.4),
                new FoodSeed("Yaourt nature", "Laitier", 61, 3.5, 4.7, 3.3)
        );
        for (FoodSeed seed : foods) {
            if (!foodRepository.existsByName(seed.name())) {
                foodRepository.save(Food.builder()
                        .name(seed.name())
                        .category(seed.category())
                        .origin("Tunisie")
                        .serving("100 g")
                        .caloriesPer100g(BigDecimal.valueOf(seed.calories()))
                        .proteinPer100g(BigDecimal.valueOf(seed.protein()))
                        .carbohydratesPer100g(BigDecimal.valueOf(seed.carbohydrates()))
                        .fatPer100g(BigDecimal.valueOf(seed.fat()))
                        .build());
            }
        }
    }

    private record FoodSeed(String name, String category, double calories, double protein,
                            double carbohydrates, double fat) {}
}