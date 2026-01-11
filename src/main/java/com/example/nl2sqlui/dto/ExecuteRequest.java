package com.example.nl2sqlui.dto;

import jakarta.validation.constraints.NotBlank;

public class ExecuteRequest {
    @NotBlank
    private String text;
    private String user_id;
    private Integer max_rows;

    public ExecuteRequest() {}

    public ExecuteRequest(String text, String userId, Integer maxRows) {
        this.text = text;
        this.user_id = userId;
        this.max_rows = maxRows;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public Integer getMax_rows() { return max_rows; }
    public void setMax_rows(Integer max_rows) { this.max_rows = max_rows; }
}
