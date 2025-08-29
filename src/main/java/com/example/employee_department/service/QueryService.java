package com.example.employee_department.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryService {
    private final JdbcTemplate jdbc;

    public QueryService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String,Object>> getYoungerEmployeesCountPerEmployee() {
        String sql = """
            SELECT 
                e1.EMP_ID,
                e1.FIRST_NAME,
                e1.LAST_NAME,
                d.DEPARTMENT_NAME,
                COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
            FROM EMPLOYEE e1
            JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
            LEFT JOIN EMPLOYEE e2
                ON e1.DEPARTMENT = e2.DEPARTMENT
               AND e2.DOB > e1.DOB
            GROUP BY 
                e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
            ORDER BY e1.EMP_ID DESC
            """;
        return jdbc.queryForList(sql);
    }
}
