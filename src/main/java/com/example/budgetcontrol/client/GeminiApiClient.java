package com.example.budgetcontrol.client;

import com.example.budgetcontrol.dto.gemini.Root;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "geminiApiClient", url = "https://generativelanguage.googleapis.com/v1beta")
public interface GeminiApiClient {

    @PostMapping("/models/gemini-1.5-flash-latest:generateContent")
    Root getData(@RequestParam("key") String apiKey, @RequestBody String content);
}
