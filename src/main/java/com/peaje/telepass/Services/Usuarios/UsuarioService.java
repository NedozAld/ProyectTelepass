package com.peaje.telepass.Services.Usuarios;

import com.peaje.telepass.Models.Entity.Role;
import org.springframework.stereotype.Service;

import com.peaje.telepass.Models.DTOs.UsuarioDTO;
import com.peaje.telepass.Models.Entity.Usuario;
import com.peaje.telepass.Models.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;

  public List<UsuarioDTO> findAll() {
    return usuarioRepository.findAll()
              .stream()
              .map(this::convertTUsuarioDTO)
              .collect(Collectors.toList());
  }
  
  public UsuarioDTO findById(Long id) {
    Optional<Usuario> usuario = usuarioRepository.findById(id);
    if (usuario.isPresent()) {
      Usuario usuarioActual = usuario.get();
      return convertTUsuarioDTO(usuarioActual);
    } else {
      return new UsuarioDTO();
    }
  }

  public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
    try {
      Usuario usuario = Usuario.builder()
              .nombre(usuarioDTO.getNombre())
              .apellido(usuarioDTO.getApellido())
              .cedula(usuarioDTO.getCedula())
              .correo(usuarioDTO.getCorreo())
              .fechaNacimiento(usuarioDTO.getFechaNacimiento())
              .contrasena(usuarioDTO.getContrasena())
              .genero(usuarioDTO.getGenero())
              .role(Role.USER)
              .build();
      usuarioRepository.save(usuario);
      return convertTUsuarioDTO(usuario);
    } catch(Exception e) {
      throw new UnsupportedOperationException("Error al guardar el usuario");
    }
  }

  public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuarioDTO) {
    Optional<Usuario> usuario = usuarioRepository.findById((id));
    if (usuario.isPresent()) {
      Usuario usuarioActual = usuario.get();
      usuarioActual.setNombre(usuarioDTO.getNombre());
      usuarioActual.setApellido(usuarioDTO.getApellido());
      usuarioActual.setCedula(usuarioDTO.getCedula());
      usuarioActual.setCorreo(usuarioDTO.getCorreo());
      usuarioActual.setContrasena(usuarioDTO.getContrasena());
      usuarioActual.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
      usuarioActual.setGenero(usuarioDTO.getGenero());
      this.usuarioRepository.save(usuarioActual);
      return convertTUsuarioDTO(usuarioActual);
    } else {
        throw new UnsupportedOperationException("El usuario no existe");
    }
  }

  public UsuarioDTO convertTUsuarioDTO(Usuario usuario) {
    return UsuarioDTO.builder()
            .id(usuario.getId())
            .nombre(usuario.getNombre())
            .apellido(usuario.getApellido())
            .cedula(usuario.getCedula())
            .correo(usuario.getCorreo())
            .contrasena(usuario.getContrasena())
            .fechaNacimiento(usuario.getFechaNacimiento())
            .genero(usuario.getGenero())
            .role(usuario.getRole().name())
            .build();
  }

  public String deleteUsuario(Long id) {
    Optional<Usuario> usuario = usuarioRepository.findById(id);
    if(usuario.isPresent()) {
      Usuario usuarioActual = usuario.get();
      usuarioRepository.delete(usuarioActual);
      return "User with ID " +id +" deleted successfully";
    }
    return "The user with ID:"+id +" does not exist";
  }
}
