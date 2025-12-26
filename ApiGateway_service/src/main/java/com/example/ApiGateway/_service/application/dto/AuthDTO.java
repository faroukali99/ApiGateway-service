package com.example.ApiGateway._service.application.dto;

import com.example.ApiGateway._service.domain.enums.UserRole;

public class AuthDTO {

    // Requête de connexion
    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {}

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Requête d'inscription
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private UserRole role;

        public RegisterRequest() {}

        public RegisterRequest(String username, String email, String password, UserRole role) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }

    // Réponse d'authentification
    public static class AuthResponse {
        private String token;
        private String username;
        private String email;
        private UserRole role;

        public AuthResponse() {}

        public AuthResponse(String token, String username, String email, UserRole role) {
            this.token = token;
            this.username = username;
            this.email = email;
            this.role = role;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }
    }
}