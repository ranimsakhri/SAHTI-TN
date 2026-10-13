package tn.sahti.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
        message = "Le mot de passe doit contenir au moins 8 caractères, avec des lettres et des chiffres"
    )
    private String password;

    @NotBlank(message = "La confirmation est obligatoire")
    private String confirmPassword;

    // CLIENT, NUTRITIONNISTE ou COACH (ADMIN interdit à l'inscription)
    private String role;
}