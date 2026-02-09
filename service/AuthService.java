package com.seuprojeto.usuarios.service;

import com.seuprojeto.usuarios.dto.LoginDTO;
import com.seuprojeto.usuarios.dto.TokenDTO;
import com.seuprojeto.usuarios.exception.SenhaInvalidaException;
import com.seuprojeto.usuarios.repository.UsuarioRepository;
import com.seuprojeto.usuarios.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public TokenDTO login(LoginDTO dto) {
        var usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(SenhaInvalidaException::new);

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new SenhaInvalidaException();
        }

        String token = tokenProvider.generateToken(usuario.getEmail());
        return new TokenDTO(token);
    }
}
