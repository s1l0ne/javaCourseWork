package com.example.nl2sqlui.dto;

import jakarta.validation.constraints.NotBlank;

public class GenerateRequest {
    @NotBlank
    private String text;
    private String user_id;

    public GenerateRequest() {}

    public GenerateRequest(String text, String userId) {
        this.text = text;
        this.user_id = userId;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
}
