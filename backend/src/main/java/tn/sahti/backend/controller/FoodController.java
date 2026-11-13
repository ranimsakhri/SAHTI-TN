package tn.sahti.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.sahti.backend.dto.FoodDto;
import tn.sahti.backend.repository.FoodRepository;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodRepository foodRepository;

    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public List<FoodDto> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category
    ) {
        String query = q == null || q.isBlank() ? null : q.trim();
        String selectedCategory = category == null || category.isBlank() ? null : category.trim();
        return foodRepository.search(query, selectedCategory, PageRequest.of(0, 100))
                .stream()
                .map(FoodDto::from)
                .toList();
    }
}
