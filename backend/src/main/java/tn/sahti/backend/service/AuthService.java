package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.AuthResponse;
import tn.sahti.backend.dto.LoginRequest;
import tn.sahti.backend.dto.RegisterRequest;
import tn.sahti.backend.entity.Role;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.entity.UserStatus;
import tn.sahti.backend.repository.UserRepository;
import tn.sahti.backend.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Les mots de passe ne correspondent pas");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cet email est déjà utilisé");
        }

        Role role = Role.CLIENT;
        if (req.getRole() != null && !req.getRole().isBlank()) {
            try {
                role = Role.valueOf(req.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle invalide");
            }
        }
        if (role == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Inscription admin interdite");
        }

        // Les professionnels attendent la validation de l'administrateur
        UserStatus status = (role == Role.CLIENT) ? UserStatus.ACTIF : UserStatus.EN_ATTENTE;

        User user = User.builder()
                .nom(req.getNom())
                .prenom(req.getPrenom())
                .email(req.getEmail().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .status(status)
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), role.name());
        return new AuthResponse(token, role.name(), user.getNom(), user.getPrenom(), user.getEmail());
    }

    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Email ou mot de passe incorrect");
        }
        if (user.getStatus() == UserStatus.SUSPENDU) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compte suspendu");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getRole().name(),
                user.getNom(), user.getPrenom(), user.getEmail());
    }
}