package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.AuthResponse;
import tn.sahti.backend.dto.LoginRequest;
import tn.sahti.backend.dto.ProRegisterRequest;
import tn.sahti.backend.dto.RegisterRequest;
import tn.sahti.backend.entity.*;
import tn.sahti.backend.repository.PasswordResetTokenRepository;
import tn.sahti.backend.repository.ProfessionalApplicationRepository;
import tn.sahti.backend.repository.UserRepository;
import tn.sahti.backend.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final ProfessionalApplicationRepository applicationRepository;
    private final FileStorageService fileStorage;

    // ---------- Inscription client ----------
    public AuthResponse register(RegisterRequest req) {
        checkPasswords(req.getPassword(), req.getConfirmPassword());
        String email = req.getEmail().trim().toLowerCase();
        checkEmailFree(email);

        User user = userRepository.save(User.builder()
                .nom(req.getNom().trim())
                .prenom(req.getPrenom().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(Role.CLIENT)
                .status(UserStatus.ACTIF)
                .build());
        return toResponse(user);
    }

    // ---------- Inscription professionnel (dossier à valider) ----------
    @Transactional
    public void registerPro(ProRegisterRequest req) {
        checkPasswords(req.getPassword(), req.getConfirmPassword());
        String email = req.getEmail().trim().toLowerCase();
        checkEmailFree(email);

        Role type;
        try {
            type = Role.valueOf(req.getType().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de professionnel invalide");
        }
        if (type != Role.NUTRITIONNISTE && type != Role.COACH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de professionnel invalide");
        }

        String cv = fileStorage.storePdf(req.getCv(), "Le CV");
        String lettre = fileStorage.storePdf(req.getLettre(), "La lettre de motivation");
        String diplome = fileStorage.storePdf(req.getDiplome(), "Le diplôme");

        User user = userRepository.save(User.builder()
                .nom(req.getNom().trim())
                .prenom(req.getPrenom().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(type)
                .status(UserStatus.EN_ATTENTE)
                .build());

        applicationRepository.save(ProfessionalApplication.builder()
                .user(user)
                .type(type)
                .specialite(req.getSpecialite().trim())
                .bio(req.getBio())
                .cvPath(cv)
                .lettrePath(lettre)
                .diplomePath(diplome)
                .status(ApplicationStatus.EN_ATTENTE)
                .build());
    }

    // ---------- Connexion ----------
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect");
        }

        switch (user.getStatus()) {
            case SUSPENDU -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Compte suspendu");
            case EN_ATTENTE -> throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Votre dossier est en cours d'examen par l'administrateur. "
                            + "Vous pourrez vous connecter dès sa validation.");
            case REFUSE -> {
                String motif = applicationRepository.findByUser(user)
                        .map(ProfessionalApplication::getMotifRefus)
                        .filter(m -> m != null && !m.isBlank())
                        .map(m -> " Motif : " + m)
                        .orElse("");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Votre candidature a été refusée." + motif);
            }
            default -> { }
        }
        return toResponse(user);
    }

    // ---------- Mot de passe oublié (démo) ----------
    public void forgotPassword(String email) {
        userRepository.findByEmail(email.trim().toLowerCase()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            resetTokenRepository.save(PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiresAt(LocalDateTime.now().plusMinutes(30))
                    .used(false)
                    .build());
            log.info("=== DEMO === Token de réinitialisation pour {} : {}", email, token);
        });
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken prt = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code invalide"));

        if (prt.isUsed() || prt.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code expiré ou déjà utilisé");
        }

        User user = prt.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        prt.setUsed(true);
        resetTokenRepository.save(prt);
    }

    // ---------- Utilitaires ----------
    private void checkPasswords(String p1, String p2) {
        if (!p1.equals(p2)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Les mots de passe ne correspondent pas");
        }
    }

    private void checkEmailFree(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
        }
    }

    private AuthResponse toResponse(User u) {
        String token = jwtUtil.generateToken(u.getEmail(), u.getRole().name());
        return new AuthResponse(token, u.getRole().name(), u.getNom(), u.getPrenom(), u.getEmail());
    }
}