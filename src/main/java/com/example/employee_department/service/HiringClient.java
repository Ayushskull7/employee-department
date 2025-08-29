package com.example.employee_department.service;

import com.example.employee_department.config.AppProperties;
import com.example.employee_department.dto.GenerateWebhookResponse;
import com.example.employee_department.dto.SubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;

@Component
public class HiringClient {
    private static final Logger log = LoggerFactory.getLogger(HiringClient.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final AppProperties props;

    public HiringClient(AppProperties props) {
        this.props = props;
    }

    public GenerateWebhookResponse generateWebhook() {
        String url = join(props.getBaseUrl(), props.getGeneratePath());
        log.info("Calling generateWebhook: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "name", props.getCandidate().getName(),
                "regNo", props.getCandidate().getRegNo(),
                "email", props.getCandidate().getEmail()
        );

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<GenerateWebhookResponse> resp =
                    restTemplate.postForEntity(url, entity, GenerateWebhookResponse.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                GenerateWebhookResponse gw = resp.getBody();
                log.info("generateWebhook - got webhook: {}, token present: {}", gw!=null ? gw.getWebhook():null, gw!=null && gw.getAccessToken()!=null);
                return gw;
            } else {
                log.error("generateWebhook returned status: {}", resp.getStatusCode());
                return null;
            }
        } catch (HttpStatusCodeException e) {
            log.error("generateWebhook http error: {} body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception ex) {
            log.error("generateWebhook failed: {}", ex.toString());
            return null;
        }
    }

    public void submitFinalQuery(String submitUrl, String token, String finalQuery) {
        if (submitUrl == null || submitUrl.isBlank()) {
            log.error("submitFinalQuery: submitUrl is null/blank");
            return;
        }
        String url = submitUrl;
        log.info("Submitting final SQL to: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String authValue = props.getAuth().isUseBearer() ? ("Bearer " + token) : token;
        if (authValue != null) headers.set(HttpHeaders.AUTHORIZATION, authValue);

        SubmitRequest req = new SubmitRequest(finalQuery);
        HttpEntity<SubmitRequest> entity = new HttpEntity<>(req, headers);

        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(url, entity, String.class);
            log.info("Submission status: {}, body: {}", resp.getStatusCode(), resp.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("Submission http error: {} body: {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception ex) {
            log.error("submitFinalQuery failed: {}", ex.toString());
        }
    }

    private String join(String base, String path) {
        if (base == null) return path;
        if (path == null) return base;
        if (path.startsWith("http")) return path;
        if (base.endsWith("/") && path.startsWith("/")) return base + path.substring(1);
        if (!base.endsWith("/") && !path.startsWith("/")) return base + "/" + path;
        return base + path;
    }
}
