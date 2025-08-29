package com.example.employee_department.dto;

public class SubmitRequest {
    private String finalQuery;
    public SubmitRequest() {}
    public SubmitRequest(String finalQuery) { this.finalQuery = finalQuery; }
    public String getFinalQuery() { return finalQuery; }
    public void setFinalQuery(String finalQuery) { this.finalQuery = finalQuery; }
}
