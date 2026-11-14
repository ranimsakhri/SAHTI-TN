package tn.sahti.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.sahti.backend.dto.CoachingRequestDto;
import tn.sahti.backend.dto.CreateCoachingRequest;
import tn.sahti.backend.service.CoachingRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/coaching-requests")
@RequiredArgsConstructor
public class CoachingRequestController {

    private final CoachingRequestService coachingRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENT')")
    public CoachingRequestDto create(
            Authentication authentication,
            @Valid @RequestBody CreateCoachingRequest request
    ) {
        return coachingRequestService.create(authentication, request);
    }

    @GetMapping("/inbox")
    @PreAuthorize("hasAnyRole('COACH', 'NUTRITIONNISTE')")
    public List<CoachingRequestDto> inbox(Authentication authentication) {
        return coachingRequestService.inbox(authentication);
    }
}
