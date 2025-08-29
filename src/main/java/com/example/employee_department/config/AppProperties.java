package com.example.employee_department.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String baseUrl;
    private String generatePath;
    private String fallbackSubmitUrl;
    private Auth auth = new Auth();
    private Candidate candidate = new Candidate();

    public static class Auth {
        private boolean useBearer = false;
        public boolean isUseBearer() { return useBearer; }
        public void setUseBearer(boolean useBearer) { this.useBearer = useBearer; }
    }

    public static class Candidate {
        private String name;
        private String regNo;
        private String email;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRegNo() { return regNo; }
        public void setRegNo(String regNo) { this.regNo = regNo; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    // getters & setters
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getGeneratePath() { return generatePath; }
    public void setGeneratePath(String generatePath) { this.generatePath = generatePath; }
    public String getFallbackSubmitUrl() { return fallbackSubmitUrl; }
    public void setFallbackSubmitUrl(String fallbackSubmitUrl) { this.fallbackSubmitUrl = fallbackSubmitUrl; }
    public Auth getAuth() { return auth; }
    public void setAuth(Auth auth) { this.auth = auth; }
    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
}
