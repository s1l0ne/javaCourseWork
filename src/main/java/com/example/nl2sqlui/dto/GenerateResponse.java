package com.example.nl2sqlui.dto;

import java.util.List;

public class GenerateResponse {
    private String sql;
    private String rationale;
    private boolean is_valid;
    private List<String> errors;

    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }

    public String getRationale() { return rationale; }
    public void setRationale(String rationale) { this.rationale = rationale; }

    public boolean isIs_valid() { return is_valid; }
    public void setIs_valid(boolean is_valid) { this.is_valid = is_valid; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
