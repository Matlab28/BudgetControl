package com.example.budgetcontrol.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BudgetRequestDto {
    private String budget;
    private String city;
    private Integer numberOfDays;
    private String plans;
    private String geminiResponse;
}
