package com.example.ApiGateway._service.infrastructure.config;

import com.example.ApiGateway._service.domain.enums.UserRole;
import com.example.ApiGateway._service.domain.model.User;
import com.example.ApiGateway._service.infrastructure.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                // Créer un utilisateur admin par défaut
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(UserRole.ROLE_ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);

                // Créer un utilisateur normal
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole(UserRole.ROLE_USER);
                user.setEnabled(true);
                userRepository.save(user);

                System.out.println("✅ Utilisateurs par défaut créés:");
                System.out.println("   - Admin: username=admin, password=admin123");
                System.out.println("   - User: username=user, password=user123");
            }
        };
    }
}