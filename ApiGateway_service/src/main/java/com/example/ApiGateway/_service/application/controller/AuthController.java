package com.example.ApiGateway._service.application.controller;

import com.example.ApiGateway._service.application.dto.AuthDTO;
import com.example.ApiGateway._service.domain.enums.UserRole;
import com.example.ApiGateway._service.domain.execption.CustomExceptions;
import com.example.ApiGateway._service.domain.model.User;
import com.example.ApiGateway._service.infrastructure.repository.UserRepository;
import com.example.ApiGateway._service.infrastructure.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDTO.AuthResponse> register(@RequestBody AuthDTO.RegisterRequest request) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomExceptions.UserAlreadyExistsException("Le nom d'utilisateur existe déjà");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomExceptions.UserAlreadyExistsException("L'email existe déjà");
        }

        // Créer un nouveau utilisateur
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : UserRole.ROLE_USER);
        user.setEnabled(true);

        userRepository.save(user);

        // Générer le token JWT
        String token = jwtUtil.generateToken(user.getUsername());

        AuthDTO.AuthResponse response = new AuthDTO.AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.AuthResponse> login(@RequestBody AuthDTO.LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomExceptions.UserNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomExceptions.InvalidCredentialsException("Mot de passe incorrect");
        }

        if (!user.getEnabled()) {
            throw new CustomExceptions.UnauthorizedAccessException("Compte désactivé");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        AuthDTO.AuthResponse response = new AuthDTO.AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return ResponseEntity.ok(jwtUtil.validateToken(token));
        }
        return ResponseEntity.ok(false);
    }
}