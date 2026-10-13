package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.ProfessionalApplicationDto;
import tn.sahti.backend.entity.*;
import tn.sahti.backend.repository.ProfessionalApplicationRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProfessionalApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorage;

    public List<ProfessionalApplicationDto> list(ApplicationStatus status) {
        List<ProfessionalApplication> items = (status == null)
                ? applicationRepository.findAllByOrderBySubmittedAtDesc()
                : applicationRepository.findByStatusOrderBySubmittedAtDesc(status);
        return items.stream().map(this::toDto).toList();
    }

    @Transactional
    public void approve(Long id) {
        ProfessionalApplication a = find(id);
        a.setStatus(ApplicationStatus.APPROUVE);
        a.setMotifRefus(null);
        a.setReviewedAt(LocalDateTime.now());
        applicationRepository.save(a);

        User u = a.getUser();
        u.setStatus(UserStatus.ACTIF);
        userRepository.save(u);
    }

    @Transactional
    public void reject(Long id, String motif) {
        ProfessionalApplication a = find(id);
        a.setStatus(ApplicationStatus.REFUSE);
        a.setMotifRefus(motif);
        a.setReviewedAt(LocalDateTime.now());
        applicationRepository.save(a);

        User u = a.getUser();
        u.setStatus(UserStatus.REFUSE);
        userRepository.save(u);
    }

    public Resource document(Long id, String doc) {
        ProfessionalApplication a = find(id);
        String file = switch (doc) {
            case "cv" -> a.getCvPath();
            case "lettre" -> a.getLettrePath();
            case "diplome" -> a.getDiplomePath();
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document inconnu");
        };
        return fileStorage.load(file);
    }

    private ProfessionalApplication find(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dossier introuvable"));
    }

    private ProfessionalApplicationDto toDto(ProfessionalApplication a) {
        User u = a.getUser();
        return new ProfessionalApplicationDto(a.getId(), u.getNom(), u.getPrenom(), u.getEmail(),
                a.getType().name(), a.getSpecialite(), a.getBio(), a.getStatus().name(),
                a.getMotifRefus(), a.getSubmittedAt());
    }
}