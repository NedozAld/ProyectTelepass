package com.peaje.telepass.Controllers.Usuarios;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reset-password")
@RequiredArgsConstructor
public class ResetController {
    
    private final UsuarioRepository usuariosRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/cambiar-contrasena")
    public void cambiarContrasena(@RequestParam String correo, @RequestParam String nuevaContrasena) {
        Optional<Usuario> usuario = usuariosRepository.findByCorreo(correo);
        if (usuario != null) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setContrasena(passwordEncoder.encode(nuevaContrasena));
            usuariosRepository.save(usuarioActual);
            
        }
    }

}
