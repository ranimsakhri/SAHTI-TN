package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.CreateMealRequest;
import tn.sahti.backend.dto.MealEntryDto;
import tn.sahti.backend.entity.MealEntry;
import tn.sahti.backend.repository.FoodRepository;
import tn.sahti.backend.repository.MealEntryRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final MealEntryRepository mealRepository;

    @Transactional(readOnly = true)
    public List<MealEntryDto> list(Authentication authentication, LocalDate date) {
        var user = currentUser(authentication);
        return mealRepository.findAllByUserIdAndDateOrderByTypeAscIdAsc(user.getId(), date)
                .stream()
                .map(MealEntryDto::from)
                .toList();
    }

    @Transactional
    public MealEntryDto create(Authentication authentication, CreateMealRequest request) {
        var user = currentUser(authentication);
        var food = foodRepository.findById(request.foodId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aliment introuvable"));
        var entry = MealEntry.builder()
                .user(user)
                .food(food)
                .type(request.type())
                .date(request.date())
                .quantityGrams(request.quantityGrams())
                .build();
        return MealEntryDto.from(mealRepository.save(entry));
    }

    @Transactional
    public void delete(Authentication authentication, Long id) {
        var user = currentUser(authentication);
        var entry = mealRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Repas introuvable"));
        mealRepository.delete(entry);
    }

    private tn.sahti.backend.entity.User currentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compte introuvable"));
    }
}
