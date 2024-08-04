package com.example.budgetcontrol.service;


import com.example.budgetcontrol.dto.gemini.Root;
import com.example.budgetcontrol.dto.request.BudgetRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface GeminiService {
    Root processUserRequest(BudgetRequestDto dto);

    Root getLatestResponse();
}
