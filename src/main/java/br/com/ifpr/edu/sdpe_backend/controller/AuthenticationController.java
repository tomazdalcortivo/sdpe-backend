package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.*;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.infra.security.TokenService;
import br.com.ifpr.edu.sdpe_backend.service.AuthorizationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final AuthorizationService authorizationService;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO data) {
        UserDetails usuario = this.authorizationService.loadUserByUsername(data.email());

        if (usuario != null) {
            if (!passwordEncoder.matches(data.senha(), usuario.getPassword())) {
                throw new BadCredentialsException("Email ou senha incorretos.");
            }
        }

        if (usuario == null) throw new UsernameNotFoundException("Usuário não encontrado");

        UsernamePasswordAuthenticationToken usuarioSenha = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        Authentication auth = this.authenticationManager.authenticate(usuarioSenha);

        String token = tokenService.generateToken((Conta) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/registrar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registrar(
            @RequestPart("dados") @Valid RegisterDTO data,
            @RequestPart(value = "arquivo") MultipartFile arquivo
    ) throws IOException {
        this.authorizationService.registrarUsuario(data, arquivo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity solicitarRecuperacao(@RequestBody @Valid EmailRequestDTO data) {
        authorizationService.solicitarRecuperacao(data.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity redefinirSenha(@RequestBody @Valid PasswordResetDTO data) {
        try {
            authorizationService.redefinirSenha(data.email(), data.codigo(), data.novaSenha());
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> getPerfilUsuarioLogado(@AuthenticationPrincipal Conta conta) {

        Participante participante = conta.getParticipante();

        Long perfilId = (participante != null) ? participante.getId() : null;

        String nome = (participante != null) ? participante.getNome() : "Usuário";
        String cidade = (participante != null) ? participante.getCidade() : "";
        String estado = (participante != null) ? participante.getEstado() : "";
        String resumo = (participante != null) ? participante.getResumo() : "";
        String telefone = (participante != null) ? participante.getTelefone() : "";
        String fotoPerfil = (participante != null) ? participante.getFotoPerfil() : null;

        UsuarioResponseDTO response = new UsuarioResponseDTO(
                perfilId,
                nome,
                conta.getEmail(),
                telefone,
                cidade,
                estado,
                resumo,
                fotoPerfil,
                conta.getPerfil()
        );

        return ResponseEntity.ok(response);
    }
}