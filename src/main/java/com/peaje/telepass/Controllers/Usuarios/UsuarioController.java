package com.peaje.telepass.Controllers.Usuarios;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peaje.telepass.Models.DTOs.UsuarioDTO;
import com.peaje.telepass.Services.Usuarios.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

}
