package tn.sahti.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProRegisterRequest {

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

    @NotBlank(message = "Le type de professionnel est obligatoire")
    private String type; // NUTRITIONNISTE ou COACH

    @NotBlank(message = "La spécialité est obligatoire")
    private String specialite;

    @Size(max = 1000, message = "La présentation est limitée à 1000 caractères")
    private String bio;

    private MultipartFile cv;
    private MultipartFile lettre;
    private MultipartFile diplome;
}