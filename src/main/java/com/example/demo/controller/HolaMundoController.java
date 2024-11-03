package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.HolaMundoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class HolaMundoController {

    private final HolaMundoService holaMundoService;
    private final UsuarioRepository usuarioRepository;

    public HolaMundoController(HolaMundoService holaMundoService,
                               UsuarioRepository usuarioRepository) {
        this.holaMundoService = holaMundoService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/hola")
    public String decirHola() {
        return holaMundoService.obtenerMensaje();
    }

    @Operation(summary = "Crear un nuevo usuario",
            description  = "Este endpoint crea un nuevo usuario y devuelve los detalles del mismo.")
    @PostMapping("/usuario")
    public Usuario crearUsuario(@Valid @RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/usuario")
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElseThrow(() -> new EntityNotFoundException("Usuario con id " + id + " no encontrado"));
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,@Valid @RequestBody Usuario detallesUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(detallesUsuario.getNombre());
            usuario.setEmail(detallesUsuario.getEmail());
            usuarioRepository.save(usuario);
            return ResponseEntity.ok(usuario);
        } else {
            throw new EntityNotFoundException("Usuario con id " + id + " no encontrado");
        }
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            usuarioRepository.delete(usuarioOpt.get());
            return ResponseEntity.ok().build();
        } else {
            throw new EntityNotFoundException("Usuario con id " + id + " no encontrado");
        }
    }
}
