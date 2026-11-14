package tn.sahti.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sahti.backend.dto.ActivityEntryDto;
import tn.sahti.backend.dto.CreateActivityRequest;
import tn.sahti.backend.entity.ActivityEntry;
import tn.sahti.backend.repository.ActivityEntryRepository;
import tn.sahti.backend.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final UserRepository userRepository;
    private final ActivityEntryRepository activityRepository;

    @Transactional(readOnly = true)
    public List<ActivityEntryDto> list(Authentication authentication, LocalDate date) {
        var user = currentUser(authentication);
        return activityRepository.findAllByUserIdAndDateOrderByIdDesc(user.getId(), date)
                .stream()
                .map(ActivityEntryDto::from)
                .toList();
    }

    @Transactional
    public ActivityEntryDto create(Authentication authentication, CreateActivityRequest request) {
        var entry = ActivityEntry.builder()
                .user(currentUser(authentication))
                .type(request.type().trim())
                .date(request.date())
                .durationMinutes(request.durationMinutes())
                .intensity(request.intensity())
                .notes(request.notes() == null || request.notes().isBlank() ? null : request.notes().trim())
                .build();
        return ActivityEntryDto.from(activityRepository.save(entry));
    }

    @Transactional
    public void delete(Authentication authentication, Long id) {
        var user = currentUser(authentication);
        var entry = activityRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activité introuvable"));
        activityRepository.delete(entry);
    }

    private tn.sahti.backend.entity.User currentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compte introuvable"));
    }
}
