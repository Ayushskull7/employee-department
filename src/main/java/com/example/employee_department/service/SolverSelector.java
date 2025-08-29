package com.example.employee_department.service;

import com.example.employee_department.config.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class SolverSelector {
    private final AppProperties props;
    private final Q1SqlSolver q1;

    public SolverSelector(AppProperties props, Q1SqlSolver q1) {
        this.props = props;
        this.q1 = q1;
    }

    public SqlSolver chooseSolver() {
        String reg = props.getCandidate().getRegNo();
        int lastTwo = extractLastTwoDigits(reg);
        boolean isOdd = (lastTwo % 2 != 0);
        return isOdd ? q1 : null;
    }

    private int extractLastTwoDigits(String s) {
        if (s == null) return 0;
        String digits = s.replaceAll("\\D", "");
        if (digits.length() == 0) return 0;
        String last2 = digits.substring(Math.max(0, digits.length() - 2));
        return Integer.parseInt(last2);
    }
}
