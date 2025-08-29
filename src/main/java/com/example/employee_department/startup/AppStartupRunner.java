package com.example.employee_department.startup;

import com.example.employee_department.config.AppProperties;
import com.example.employee_department.dto.GenerateWebhookResponse;
import com.example.employee_department.service.HiringClient;
import com.example.employee_department.service.SqlSolver;
import com.example.employee_department.service.SolverSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);
    private final HiringClient client;
    private final SolverSelector selector;
    private final AppProperties props;

    public AppStartupRunner(HiringClient client, SolverSelector selector, AppProperties props) {
        this.client = client; this.selector = selector; this.props = props;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== Boot flow started at {} ===", Instant.now());

        // 1) Call generateWebhook
        GenerateWebhookResponse resp = client.generateWebhook();
        if (resp == null || resp.getAccessToken() == null) {
            log.error("Failed to get access token/webhook. Aborting.");
            return;
        }

        // 2) choose & build final SQL query
        SqlSolver solver = selector.chooseSolver();
        String finalQuery = solver.buildFinalQuery();
        if (finalQuery == null || finalQuery.isBlank()) {
            log.error("Final query is empty. Aborting.");
            return;
        }

        // 3) persist locally
        Path out = Path.of("target", "solution.sql");
        Files.createDirectories(out.getParent());
        Files.writeString(out, finalQuery + System.lineSeparator());
        log.info("Saved final SQL to {}", out.toAbsolutePath());

        // 4) submit using returned webhook or fallback URL
        String submitUrl = (resp.getWebhook() != null && !resp.getWebhook().isBlank())
                ? resp.getWebhook()
                : props.getFallbackSubmitUrl();

        client.submitFinalQuery(submitUrl, resp.getAccessToken(), finalQuery);

        log.info("=== Boot flow completed ===");
    }
}
