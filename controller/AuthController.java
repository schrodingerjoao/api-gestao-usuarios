package com.seuprojeto.usuarios.controller;

import com.seuprojeto.usuarios.dto.LoginDTO;
import com.seuprojeto.usuarios.dto.TokenDTO;
import com.seuprojeto.usuarios.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginDTO dto) {
        return authService.login(dto);
    }
}
