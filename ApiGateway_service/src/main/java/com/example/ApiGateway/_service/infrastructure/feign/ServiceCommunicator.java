package com.example.ApiGateway._service.infrastructure.feign;

import com.example.ApiGateway._service.application.dto.ServiceDTO;
import com.example.ApiGateway._service.domain.execption.CustomExceptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceCommunicator {

    private final RestTemplate restTemplate;

    @Value("${services.user.url:http://localhost:8081}")
    private String userServiceUrl;

    @Value("${services.product.url:http://localhost:8082}")
    private String productServiceUrl;

    @Value("${services.order.url:http://localhost:8083}")
    private String orderServiceUrl;

    public ServiceCommunicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ServiceDTO.ServiceResponse<?> forwardToService(
            String serviceName,
            String endpoint,
            Object data,
            String authHeader) {

        try {
            String serviceUrl = getServiceUrl(serviceName);
            String fullUrl = serviceUrl + endpoint;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.set("Content-Type", "application/json");

            HttpEntity<?> requestEntity = new HttpEntity<>(data, headers);

            ResponseEntity<ServiceDTO.ServiceResponse> response = restTemplate.exchange(
                    fullUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ServiceDTO.ServiceResponse.class
            );

            return response.getBody();

        } catch (Exception e) {
            throw new CustomExceptions.ServiceCommunicationException(
                    "Erreur lors de la communication avec le service: " + serviceName, e);
        }
    }

    public Map<String, Boolean> checkServicesHealth() {
        Map<String, Boolean> healthStatus = new HashMap<>();

        healthStatus.put("user-service", checkServiceHealth(userServiceUrl));
        healthStatus.put("product-service", checkServiceHealth(productServiceUrl));
        healthStatus.put("order-service", checkServiceHealth(orderServiceUrl));

        return healthStatus;
    }

    private boolean checkServiceHealth(String serviceUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    serviceUrl + "/actuator/health",
                    String.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    private String getServiceUrl(String serviceName) {
        return switch (serviceName.toLowerCase()) {
            case "user", "user-service" -> userServiceUrl;
            case "product", "product-service" -> productServiceUrl;
            case "order", "order-service" -> orderServiceUrl;
            default -> throw new CustomExceptions.ServiceCommunicationException(
                    "Service inconnu: " + serviceName);
        };
    }
}