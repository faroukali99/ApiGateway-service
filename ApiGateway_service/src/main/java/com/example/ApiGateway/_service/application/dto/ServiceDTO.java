package com.example.ApiGateway._service.application.dto;

public class ServiceDTO {

    // DTO pour la communication avec d'autres services
    public static class ServiceRequest {
        private String serviceName;
        private String endpoint;
        private Object data;

        public ServiceRequest() {}

        public ServiceRequest(String serviceName, String endpoint, Object data) {
            this.serviceName = serviceName;
            this.endpoint = endpoint;
            this.data = data;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    // Réponse générique des services
    public static class ServiceResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public ServiceResponse() {}

        public ServiceResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}