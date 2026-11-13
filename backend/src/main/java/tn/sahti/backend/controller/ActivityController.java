package tn.sahti.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.sahti.backend.dto.ActivityEntryDto;
import tn.sahti.backend.dto.CreateActivityRequest;
import tn.sahti.backend.service.ActivityService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public List<ActivityEntryDto> list(Authentication authentication,
                                       @RequestParam(required = false) LocalDate date) {
        return activityService.list(authentication, date == null ? LocalDate.now() : date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityEntryDto create(Authentication authentication,
                                   @Valid @RequestBody CreateActivityRequest request) {
        return activityService.create(authentication, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable Long id) {
        activityService.delete(authentication, id);
    }
}
