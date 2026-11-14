package tn.sahti.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.ProfileRequest;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.ClientProfileRepository;
import tn.sahti.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientProfileServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientProfileRepository profileRepository;
    @InjectMocks
    private ClientProfileService service;

    @Test
    void savesProfileForTheAuthenticatedAccount() {
        User user = User.builder().id(5L).email("client@example.com").prenom("Amel").nom("Ben Ali").build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(profileRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(profileRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ProfileRequest request = new ProfileRequest(
                "FEMME", LocalDate.of(1995, 3, 10), BigDecimal.valueOf(165), BigDecimal.valueOf(62),
                "MAINTENIR", "MODERE", "Végétarienne", null
        );

        var result = service.save(authentication(user.getEmail()), request);

        assertEquals(user.getEmail(), result.email());
        assertEquals(request.birthDate(), result.birthDate());
        assertEquals(request.activityLevel(), result.activityLevel());
        assertEquals(request.dietaryPreferences(), result.dietaryPreferences());
        verify(profileRepository).save(any());
    }

    @Test
    void rejectsUnrealisticBirthDate() {
        ProfileRequest request = new ProfileRequest(
                "FEMME", LocalDate.of(1800, 1, 1), BigDecimal.valueOf(165), BigDecimal.valueOf(62),
                "MAINTENIR", "MODERE", null, null
        );

        assertThrows(ResponseStatusException.class,
                () -> service.save(authentication("client@example.com"), request));
        verifyNoInteractions(userRepository, profileRepository);
    }

    private Authentication authentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }
}
