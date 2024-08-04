package com.example.budgetcontrol.service;

import com.example.budgetcontrol.client.GeminiApiClient;
import com.example.budgetcontrol.dto.gemini.Candidate;
import com.example.budgetcontrol.dto.gemini.Root;
import com.example.budgetcontrol.dto.request.BudgetRequestDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BudgetService implements GeminiService {
    private final String key = "AIzaSyB3GRRnXnOsnRJOhPpLQtWvKf4Bs1Hx-nU";
    private final GeminiApiClient client;
    private Root latestResponse;

    public BudgetService(GeminiApiClient client) {
        this.client = client;
    }

    @Override
    public Root processUserRequest(BudgetRequestDto dto) {

        if (!dto.getBudget().matches(".*\\d+.*")) {
            log.error("Invalid budget info.");
            throw new RuntimeException("Please enter only numbers...");
        }

        String instruction = constructInstruction(dto);
        Root updates = getUpdates(instruction);
        String extractedText = extractTextFromGeminiResponse(updates);
        dto.setGeminiResponse(extractedText);

        log.info("Budget info created.");

        latestResponse = updates;
        return latestResponse;
    }

    @Override
    public Root getLatestResponse() {
        return latestResponse;
    }

    private String constructInstruction(BudgetRequestDto dto) {
        StringBuilder instruction = new StringBuilder();
        instruction.append("User's budget: ").append(dto.getBudget()).append("\n");
        instruction.append("City: ").append(dto.getCity()).append("\n");
        instruction.append("User's plans: ").append(dto.getPlans()).append("\n");
        instruction.append("Time frame: ").append(dto.getNumberOfDays()).append(" days").append("\n");
        instruction.append("Please provide a budget allocation plan based on the given information.");
        return instruction.toString();
    }

    private Root getUpdates(String instruction) {
        try {
            JsonObject json = new JsonObject();
            JsonArray contentsArray = new JsonArray();
            JsonObject contentsObject = new JsonObject();
            JsonArray partsArray = new JsonArray();
            JsonObject partsObject = new JsonObject();

            partsObject.add("text", new JsonPrimitive(instruction));
            partsArray.add(partsObject);
            contentsObject.add("parts", partsArray);
            contentsArray.add(contentsObject);
            json.add("contents", contentsArray);

            String content = json.toString();
            return client.getData(key, content);
        } catch (Exception e) {
            log.error("Error while getting updates from Gemini API: ", e);
            throw e;
        }
    }

    private String extractTextFromGeminiResponse(Root updates) {
        StringBuilder textBuilder = new StringBuilder();

        if (updates.getCandidates() != null) {
            for (Candidate candidate : updates.getCandidates()) {
                String text = candidate.getContent().getParts().get(0).getText();
                text = text.replace("*", "");
                textBuilder.append(text).append("\n\n");
            }
        }

        String response = textBuilder.toString().trim();

        return response
                .replaceFirst("^##", "")
                .replace("##", "\n##")
                .replace("Expenses:", "\nExpenses:\n")
                .replace("Total Expenses:", "\nTotal Expenses:")
                .replace("Remaining Budget:", "\nRemaining Budget:")
                .replace("Budget Allocation Plan:", "\nBudget Allocation Plan:\n")
                .replace("Important Considerations:", "\nImportant Considerations:\n");
    }
}
