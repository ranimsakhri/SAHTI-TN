
package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void envoyerCodeReinitialisation(
            String destinataire, String token) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(destinataire);
        message.setSubject("SAHTI.TN - Réinitialisation du mot de passe");

        message.setText(
                "Bonjour,\n\n"
                        + "Vous avez demandé la réinitialisation de votre "
                        + "mot de passe SAHTI.TN.\n\n"
                        + "Votre code de réinitialisation est :\n\n"
                        + token + "\n\n"
                        + "Ce code expire dans 30 minutes et ne peut être "
                        + "utilisé qu'une seule fois.\n\n"
                        + "Si vous n'avez pas demandé cette réinitialisation, "
                        + "ignorez cet email.\n\n"
                        + "L'équipe SAHTI.TN"
        );

        mailSender.send(message);
    }
}
