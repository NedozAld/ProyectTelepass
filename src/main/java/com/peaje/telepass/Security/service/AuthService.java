package com.peaje.telepass.Security.service;

import com.peaje.telepass.Models.DTOs.UsuarioDTO;
import com.peaje.telepass.Security.model.AuthRequest;
import com.peaje.telepass.Security.model.AuthResponse;

public interface AuthService {
    AuthResponse register(UsuarioDTO userDTO);

    AuthResponse authenticate(AuthRequest request);
}
