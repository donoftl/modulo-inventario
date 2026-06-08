package com.prueba.tecnica.Sof.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.tecnica.Sof.dto.AuthRequestDTO;
import com.prueba.tecnica.Sof.dto.AuthResponseDTO;
import com.prueba.tecnica.Sof.exception.BusinessException;
import com.prueba.tecnica.Sof.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // Simulación de credenciales fijas para agilizar la prueba técnica
        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtUtil.crearToken(username, "ADMIN");
            return ResponseEntity.ok(new AuthResponseDTO(token, username, "ADMIN"));
        } else if ("user".equals(username) && "user123".equals(password)) {
            String token = jwtUtil.crearToken(username, "USER");
            return ResponseEntity.ok(new AuthResponseDTO(token, username, "USER"));
        } else {
            throw new BusinessException("Credenciales inválidas. Usuario o contraseña incorrectos.");
        }
    }
}