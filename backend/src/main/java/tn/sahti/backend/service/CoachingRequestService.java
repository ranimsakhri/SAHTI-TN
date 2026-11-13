package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.CoachingRequestDto;
import tn.sahti.backend.dto.CreateCoachingRequest;
import tn.sahti.backend.entity.CoachingRequest;
import tn.sahti.backend.entity.CoachingRequestStatus;
import tn.sahti.backend.entity.Role;
import tn.sahti.backend.repository.CoachingRequestRepository;
import tn.sahti.backend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachingRequestService {

    private final CoachingRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional
    public CoachingRequestDto create(Authentication authentication, CreateCoachingRequest request) {
        if (request.type() != Role.NUTRITIONNISTE && request.type() != Role.COACH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type de professionnel invalide");
        }

        var client = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compte client introuvable"));
        var coachingRequest = CoachingRequest.builder()
                .client(client)
                .professionalType(request.type())
                .message(request.message() == null ? null : request.message().trim())
                .status(CoachingRequestStatus.EN_ATTENTE)
                .build();
        return CoachingRequestDto.from(requestRepository.save(coachingRequest));
    }

    @Transactional(readOnly = true)
    public List<CoachingRequestDto> inbox(Authentication authentication) {
        Role professionalType = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(authority -> authority.equals("ROLE_COACH") || authority.equals("ROLE_NUTRITIONNISTE"))
                .map(authority -> Role.valueOf(authority.substring("ROLE_".length())))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Accès réservé aux professionnels"));

        return requestRepository.findAllByProfessionalTypeOrderByCreatedAtDesc(professionalType)
                .stream()
                .map(CoachingRequestDto::from)
                .toList();
    }
}
