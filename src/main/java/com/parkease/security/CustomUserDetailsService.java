package com.parkease.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.parkease.entity.User;
import com.parkease.repository.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User Not Found"));
        
        System.out.println(
                "LOADED USER ROLE = "
                + user.getRole());

        return org.springframework.security.core.userdetails
                .User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(
                        user.getRole()
                                .replace("ROLE_", ""))
                .build();
    }
}