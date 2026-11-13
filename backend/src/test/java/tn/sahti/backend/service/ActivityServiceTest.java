package tn.sahti.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tn.sahti.backend.dto.CreateActivityRequest;
import tn.sahti.backend.entity.ActivityEntry;
import tn.sahti.backend.entity.ActivityIntensity;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.ActivityEntryRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivityEntryRepository activityRepository;
    @InjectMocks
    private ActivityService service;

    @Test
    void createsActivityForTheAuthenticatedUser() {
        User user = User.builder().id(6L).email("client@example.com").build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(activityRepository.save(any(ActivityEntry.class))).thenAnswer(invocation -> {
            ActivityEntry entry = invocation.getArgument(0);
            entry.setId(11L);
            return entry;
        });

        var result = service.create(authentication(user.getEmail()),
                new CreateActivityRequest("  Marche  ", LocalDate.now(), 35, ActivityIntensity.MODEREE, "  Parc  "));

        assertEquals(11L, result.id());
        assertEquals("Marche", result.type());
        assertEquals("Parc", result.notes());
        verify(activityRepository).save(any(ActivityEntry.class));
    }

    private Authentication authentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }
}
