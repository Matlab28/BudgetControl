package com.example.budgetcontrol.controller;

import com.example.budgetcontrol.dto.request.BudgetRequestDto;
import com.example.budgetcontrol.dto.response.BudgetResponseDto;
import com.example.budgetcontrol.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin(origins = "https://matlab28.github.io")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    @PostMapping
    @CrossOrigin(origins = "https://matlab28.github.io")
    public ResponseEntity<BudgetResponseDto> processBudget(@RequestBody BudgetRequestDto requestDto) {
        try {
            budgetService.processUserRequest(requestDto);
            return ResponseEntity.ok(new BudgetResponseDto(requestDto.getGeminiResponse()));
        } catch (Exception e) {
            logger.error("Error processing budget request", e);
            return ResponseEntity.status(500).body(new BudgetResponseDto("Internal server error: " + e.getMessage()));
        }
    }
}
