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
import tn.sahti.backend.dto.CreateMealRequest;
import tn.sahti.backend.entity.Food;
import tn.sahti.backend.entity.MealEntry;
import tn.sahti.backend.entity.MealType;
import tn.sahti.backend.entity.User;
import tn.sahti.backend.repository.FoodRepository;
import tn.sahti.backend.repository.MealEntryRepository;
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
class MealServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private MealEntryRepository mealRepository;
    @InjectMocks
    private MealService service;

    @Test
    void createsMealForTheAuthenticatedUserAndUsesCatalogCalories() {
        User user = User.builder().id(1L).email("client@example.com").build();
        Food food = Food.builder().id(3L).name("Lablabi").caloriesPer100g(BigDecimal.valueOf(125)).build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(foodRepository.findById(3L)).thenReturn(Optional.of(food));
        when(mealRepository.save(any(MealEntry.class))).thenAnswer(invocation -> {
            MealEntry entry = invocation.getArgument(0);
            entry.setId(7L);
            return entry;
        });

        var result = service.create(authentication(user.getEmail()),
                new CreateMealRequest(3L, MealType.DEJEUNER, LocalDate.now(), BigDecimal.valueOf(200)));

        assertEquals("Lablabi", result.foodName());
        assertEquals(BigDecimal.valueOf(250), result.caloriesEstimated());
        verify(mealRepository).save(any(MealEntry.class));
    }

    @Test
    void cannotDeleteAnotherUsersMeal() {
        User user = User.builder().id(2L).email("other@example.com").build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mealRepository.findByIdAndUserId(7L, user.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.delete(authentication(user.getEmail()), 7L));
        verify(mealRepository, never()).delete(any(MealEntry.class));
    }

    private Authentication authentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, null,
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
    }
}
