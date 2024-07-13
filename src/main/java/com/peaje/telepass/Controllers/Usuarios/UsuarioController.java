package com.peaje.telepass.Controllers.Usuarios;

import com.peaje.telepass.Models.Entity.Usuario;
import org.springframework.web.bind.annotation.*;

import com.peaje.telepass.Models.DTOs.UsuarioDTO;
import com.peaje.telepass.Services.Usuarios.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

  private final UsuarioService usuarioService;

  @GetMapping()
  public ResponseEntity<java.util.List<UsuarioDTO>> getAllUser() {
    return ResponseEntity.ok(usuarioService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDTO> getUserById(@PathVariable Long id) {
    UsuarioDTO usuario = usuarioService.findById(id);
    return ResponseEntity.ok(usuarioService.findById(id));
  }

  @PostMapping()
  public ResponseEntity<UsuarioDTO> createUser(@RequestBody UsuarioDTO usuarioDTO) {
      return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(usuarioDTO));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UsuarioDTO> updateUser(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
    UsuarioDTO usuario = usuarioService.findById(id);
    if(usuario.getId() == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(usuarioService.updateUsuario(id, usuarioDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable Long id) {
    UsuarioDTO usuario = usuarioService.findById(id);
    usuarioService.deleteUsuario(id);
    return ResponseEntity.noContent().build();
  }

}
