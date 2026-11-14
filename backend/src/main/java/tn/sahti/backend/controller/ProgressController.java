package tn.sahti.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.sahti.backend.dto.DailyProgressDto;
import tn.sahti.backend.service.ProgressService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CLIENT')")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping
    public DailyProgressDto daily(Authentication authentication,
                                  @RequestParam(required = false) LocalDate date) {
        return progressService.daily(authentication, date == null ? LocalDate.now() : date);
    }
}
