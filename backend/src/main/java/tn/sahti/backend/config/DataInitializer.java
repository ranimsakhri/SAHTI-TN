package tn.sahti.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.sahti.backend.entity.Role;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.entity.UserStatus;
import tn.sahti.backend.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
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
    }
}