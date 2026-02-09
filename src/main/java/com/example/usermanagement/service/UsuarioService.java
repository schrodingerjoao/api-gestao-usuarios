package com.example.usermanagement.service;

import com.example.usermanagement.dto.*;
import com.example.usermanagement.exception.EmailJaExisteException;
import com.example.usermanagement.exception.SenhaInvalidaException;
import com.example.usermanagement.exception.UsuarioNaoEncontradoException;
import com.example.usermanagement.model.Usuario;
import com.example.usermanagement.repository.UsuarioRepository;
import com.example.usermanagement.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenDTO login(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
            throw new SenhaInvalidaException("Senha inválida");
        }

        String token = jwtTokenProvider.generateToken(usuario.getEmail());
        return TokenDTO.builder()
                .token(token)
                .tipo("Bearer")
                .expiracaoEm(86400000L)
                .build();
    }

    public UsuarioDTO criarUsuario(CriarUsuarioDTO criarDTO) {
        if (usuarioRepository.existsByEmail(criarDTO.getEmail())) {
            throw new EmailJaExisteException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(criarDTO.getNome())
                .email(criarDTO.getEmail())
                .senha(passwordEncoder.encode(criarDTO.getSenha()))
                .telefone(criarDTO.getTelefone())
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);
        return mapToDTO(usuario);
    }

    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO obterPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado"));
        return mapToDTO(usuario);
    }

    public UsuarioDTO obterPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com email " + email + " não encontrado"));
        return mapToDTO(usuario);
    }

    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado"));

        if (usuarioDTO.getNome() != null) {
            usuario.setNome(usuarioDTO.getNome());
        }
        if (usuarioDTO.getTelefone() != null) {
            usuario.setTelefone(usuarioDTO.getTelefone());
        }
        if (usuarioDTO.getAtivo() != null) {
            usuario.setAtivo(usuarioDTO.getAtivo());
        }

        usuarioRepository.save(usuario);
        return mapToDTO(usuario);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .ativo(usuario.getAtivo())
                .build();
    }
}
