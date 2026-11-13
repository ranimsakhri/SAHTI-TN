package tn.sahti.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.sahti.backend.dto.CreateMealRequest;
import tn.sahti.backend.dto.MealEntryDto;
import tn.sahti.backend.service.MealService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class MealController {

    private final MealService mealService;

    @GetMapping
    public List<MealEntryDto> list(Authentication authentication,
                                   @RequestParam(required = false) LocalDate date) {
        return mealService.list(authentication, date == null ? LocalDate.now() : date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealEntryDto create(Authentication authentication, @Valid @RequestBody CreateMealRequest request) {
        return mealService.create(authentication, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable Long id) {
        mealService.delete(authentication, id);
    }
}
