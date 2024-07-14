package com.peaje.telepass.Security.controller;

import com.peaje.telepass.Models.DTOs.UsuarioDTO;
import com.peaje.telepass.Security.model.AuthRequest;
import com.peaje.telepass.Security.model.AuthResponse;
import com.peaje.telepass.Security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello from API AUTH-USER";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UsuarioDTO userDTO) {
        return ResponseEntity.ok(authService.register(userDTO));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
