package com.example.ApiGateway._service.application.controller;

import com.example.ApiGateway._service.application.dto.ServiceDTO;
import com.example.ApiGateway._service.infrastructure.feign.ServiceCommunicator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final ServiceCommunicator serviceCommunicator;

    public GatewayController(ServiceCommunicator serviceCommunicator) {
        this.serviceCommunicator = serviceCommunicator;
    }

    @PostMapping("/forward")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'MANAGER')")
    public ResponseEntity<ServiceDTO.ServiceResponse<?>> forwardRequest(
            @RequestBody ServiceDTO.ServiceRequest request,
            @RequestHeader("Authorization") String authHeader) {

        ServiceDTO.ServiceResponse<?> response = serviceCommunicator.forwardToService(
                request.getServiceName(),
                request.getEndpoint(),
                request.getData(),
                authHeader
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("API Gateway is running");
    }

    @GetMapping("/services/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getServicesStatus() {
        return ResponseEntity.ok(serviceCommunicator.checkServicesHealth());
    }
}