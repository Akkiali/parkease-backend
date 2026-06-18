package com.parkease.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parkease.dto.AuthResponse;
import com.parkease.dto.LoginRequest;
import com.parkease.dto.RegisterRequest;
import com.parkease.entity.User;
import com.parkease.repository.UserRepository;
import com.parkease.security.JwtUtil;
import java.time.LocalDateTime;
import java.util.UUID;

import com.parkease.entity.PasswordResetToken;
import com.parkease.repository.PasswordResetTokenRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            PasswordResetTokenRepository tokenRepository,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public String register(
            RegisterRequest request) {

        User user = new User();

        user.setFullName(
                request.getFullName());

        user.setEmail(
                request.getEmail());

        user.setMobile(
                request.getMobile());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole(
                "ROLE_USER");

        userRepository.save(user);

        return "User Registered Successfully";
    }

    public AuthResponse login(
            LoginRequest request) {

        User user =
                userRepository.findByEmail(
                        request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid Password");
        }

        AuthResponse response =
                new AuthResponse();
        
        response.setId(
                user.getId());
        
        

        response.setToken(
                jwtUtil.generateToken(
                        user.getEmail()));

        response.setEmail(
                user.getEmail());

        response.setRole(
                user.getRole());
        
        response.setFullName(
                user.getFullName());


        return response;
    }
    
    public String forgotPassword(
            String email) {

        User user =
                userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));
        
        tokenRepository.findByUser(user)
        .ifPresent(
              tokenRepository::delete);
        
        String token =
        	      UUID.randomUUID().toString();

        	PasswordResetToken resetToken =
        	      new PasswordResetToken();

        	resetToken.setToken(token);
        	resetToken.setUser(user);
        	resetToken.setExpiryDate(
        	      LocalDateTime.now()
        	            .plusMinutes(15));

        	tokenRepository.save(resetToken);

        String link =
                "http://localhost:5173/reset-password?token="
                        + token;

        emailService.sendEmail(
                user.getEmail(),
                "Reset Password",
                "Click the link below - valid for 15 minutes:\n\n"
                        + link);

        return "Reset link sent successfully";
    }

    public String resetPassword(
            String token,
            String password) {

        PasswordResetToken resetToken =
                tokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid Token"));

        if (resetToken.getExpiryDate()
                .isBefore(
                        LocalDateTime.now())) {

            throw new RuntimeException(
                    "Token Expired");
        }

        User user =
                resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(
                        password));

        userRepository.save(user);

        tokenRepository.delete(
                resetToken);

        return "Password Updated";
    }
}