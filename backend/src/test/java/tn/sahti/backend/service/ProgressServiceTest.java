package tn.sahti.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tn.sahti.backend.entity.*;
import tn.sahti.backend.repository.ActivityEntryRepository;
import tn.sahti.backend.repository.MealEntryRepository;
import tn.sahti.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MealEntryRepository mealRepository;
    @Mock
    private ActivityEntryRepository activityRepository;
    @InjectMocks
    private ProgressService service;

    @Test
    void aggregatesCaloriesAndActivityForTheSelectedDay() {
        LocalDate date = LocalDate.of(2026, 11, 10);
        User user = User.builder().id(1L).email("client@example.com").build();
        Food food = Food.builder().id(9L).name("Lablabi").caloriesPer100g(BigDecimal.valueOf(125)).build();
        MealEntry meal = MealEntry.builder()
                .id(2L).user(user).food(food).type(MealType.DEJEUNER)
                .date(date).quantityGrams(BigDecimal.valueOf(200)).build();
        ActivityEntry walk = ActivityEntry.builder()
                .id(3L).user(user).type("Marche").date(date).durationMinutes(35)
                .intensity(ActivityIntensity.MODEREE).build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mealRepository.findAllByUserIdAndDateOrderByTypeAscIdAsc(user.getId(), date))
                .thenReturn(List.of(meal));
        when(activityRepository.findAllByUserIdAndDateOrderByIdDesc(user.getId(), date))
                .thenReturn(List.of(walk));

        var result = service.daily(authentication(user.getEmail()), date);

        assertEquals(date, result.date());
        assertEquals(BigDecimal.valueOf(250), result.caloriesEstimated());
        assertEquals(1, result.mealEntries());
        assertEquals(35, result.activeMinutes());
        assertEquals(1, result.activities());
    }

    private Authentication authentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }
}
