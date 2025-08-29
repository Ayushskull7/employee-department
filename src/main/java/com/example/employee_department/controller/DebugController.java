package com.example.employee_department.controller;

import com.example.employee_department.service.QueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugController {
    private final QueryService queryService;

    public DebugController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/younger-employees")
    public List<Map<String,Object>> getYoungerEmployees() {
        return queryService.getYoungerEmployeesCountPerEmployee();
    }
}
