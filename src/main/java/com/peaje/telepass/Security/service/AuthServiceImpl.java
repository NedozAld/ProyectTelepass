package com.peaje.telepass.Security.service;

import com.peaje.telepass.Models.DTOs.UsuarioDTO;
import com.peaje.telepass.Models.Entity.Role;
import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Repository.UsuarioRepository;
import com.peaje.telepass.Security.model.AuthRequest;
import com.peaje.telepass.Security.model.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(UsuarioDTO userDTO) {
        var user = Usuario.builder()
                .nombre(userDTO.getNombre())
                .apellido(userDTO.getApellido())
                .correo(userDTO.getCorreo())
                .cedula(userDTO.getCedula())
                .contrasena(passwordEncoder.encode(userDTO.getContrasena()))
                .fechaNacimiento(userDTO.getFechaNacimiento())
                .genero(userDTO.getGenero())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena()));
        var user = userRepository.findByCorreo(request.getCorreo()).orElseThrow();
        var token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .build();
    }
}
