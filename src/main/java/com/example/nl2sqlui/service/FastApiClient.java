package com.example.nl2sqlui.service;

import com.example.nl2sqlui.config.AppProperties;
import com.example.nl2sqlui.dto.ExecuteRequest;
import com.example.nl2sqlui.dto.ExecuteResponse;
import com.example.nl2sqlui.dto.GenerateRequest;
import com.example.nl2sqlui.dto.GenerateResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class FastApiClient {

    private final WebClient webClient;
    private final AppProperties props;

    public FastApiClient(WebClient.Builder builder, AppProperties props) {
        this.props = props;
        this.webClient = builder.baseUrl(props.getFastapi().getBaseUrl()).build();
    }

    public GenerateResponse generate(String text) {
        try {
            return webClient.post()
                    .uri("/api/v1/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new GenerateRequest(text, props.getFastapi().getUserId()))
                    .retrieve()
                    .bodyToMono(GenerateResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("FastAPI /generate error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        }
    }

    public ExecuteResponse execute(String text, Integer maxRows) {
        try {
            return webClient.post()
                    .uri("/api/v1/execute")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new ExecuteRequest(text, props.getFastapi().getUserId(), maxRows))
                    .retrieve()
                    .bodyToMono(ExecuteResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("FastAPI /execute error: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        }
    }
}
