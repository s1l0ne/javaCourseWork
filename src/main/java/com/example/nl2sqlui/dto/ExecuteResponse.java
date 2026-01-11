package com.example.nl2sqlui.dto;

import java.util.List;
import java.util.Map;

public class ExecuteResponse {
    private String sql;
    private List<String> columns;
    private List<Map<String, Object>> rows;
    private int rows_returned;
    private int duration_ms;
    private boolean is_valid;
    private List<String> errors;

    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }

    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }

    public List<Map<String, Object>> getRows() { return rows; }
    public void setRows(List<Map<String, Object>> rows) { this.rows = rows; }

    public int getRows_returned() { return rows_returned; }
    public void setRows_returned(int rows_returned) { this.rows_returned = rows_returned; }

    public int getDuration_ms() { return duration_ms; }
    public void setDuration_ms(int duration_ms) { this.duration_ms = duration_ms; }

    public boolean isIs_valid() { return is_valid; }
    public void setIs_valid(boolean is_valid) { this.is_valid = is_valid; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
