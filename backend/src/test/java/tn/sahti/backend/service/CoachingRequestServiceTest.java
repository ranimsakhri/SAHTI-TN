package tn.sahti.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.CreateCoachingRequest;
import tn.sahti.backend.entity.CoachingRequest;
import tn.sahti.backend.entity.Role;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.CoachingRequestRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachingRequestServiceTest {

    @Mock
    private CoachingRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CoachingRequestService service;

    @Test
    void createsRequestForAuthenticatedClient() {
        User client = User.builder()
                .nom("Ben Ali")
                .prenom("Amel")
                .email("client@example.com")
                .build();
        when(userRepository.findByEmail("client@example.com")).thenReturn(Optional.of(client));
        when(requestRepository.save(any(CoachingRequest.class))).thenAnswer(invocation -> {
            CoachingRequest request = invocation.getArgument(0);
            request.setId(12L);
            request.setCreatedAt(LocalDateTime.now());
            return request;
        });

        var result = service.create(authentication("client@example.com", "ROLE_CLIENT"),
                new CreateCoachingRequest(Role.COACH, "Reprendre le sport"));

        assertEquals(12L, result.id());
        assertEquals("Amel", result.clientFirstName());
        assertEquals("COACH", result.type());
        assertEquals("Reprendre le sport", result.message());
        verify(requestRepository).save(any(CoachingRequest.class));
    }

    @Test
    void rejectsUnsupportedProfessionalType() {
        assertThrows(ResponseStatusException.class, () -> service.create(
                authentication("client@example.com", "ROLE_CLIENT"),
                new CreateCoachingRequest(Role.ADMIN, "Demande invalide")));
        verifyNoInteractions(userRepository, requestRepository);
    }

    @Test
    void listsOnlyRequestsForTheAuthenticatedProfessionalType() {
        when(requestRepository.findAllByProfessionalTypeOrderByCreatedAtDesc(Role.NUTRITIONNISTE))
                .thenReturn(List.of());

        assertTrue(service.inbox(authentication("pro@example.com", "ROLE_NUTRITIONNISTE")).isEmpty());

        verify(requestRepository).findAllByProfessionalTypeOrderByCreatedAtDesc(Role.NUTRITIONNISTE);
        verifyNoInteractions(userRepository);
    }

    private UsernamePasswordAuthenticationToken authentication(String email, String authority) {
        return new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(authority)));
    }
}
