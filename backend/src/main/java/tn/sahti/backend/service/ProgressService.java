package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.DailyProgressDto;
import tn.sahti.backend.dto.MealEntryDto;
import tn.sahti.backend.repository.ActivityEntryRepository;
import tn.sahti.backend.repository.MealEntryRepository;
import tn.sahti.backend.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final UserRepository userRepository;
    private final MealEntryRepository mealRepository;
    private final ActivityEntryRepository activityRepository;

    @Transactional(readOnly = true)
    public DailyProgressDto daily(Authentication authentication, LocalDate date) {
        var user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compte introuvable"));
        List<MealEntryDto> meals = mealRepository
                .findAllByUserIdAndDateOrderByTypeAscIdAsc(user.getId(), date)
                .stream()
                .map(MealEntryDto::from)
                .toList();
        var activities = activityRepository.findAllByUserIdAndDateOrderByIdDesc(user.getId(), date);
        int activeMinutes = activities.stream().mapToInt(activity -> activity.getDurationMinutes()).sum();
        BigDecimal calories = meals.stream()
                .map(MealEntryDto::caloriesEstimated)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DailyProgressDto(date, calories, meals.size(), activeMinutes, activities.size());
    }
}
