package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.*;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.exception.EntityNotFoundException;
import br.com.ifpr.edu.sdpe_backend.infra.security.TokenService;
import br.com.ifpr.edu.sdpe_backend.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO data) {
        UsernamePasswordAuthenticationToken usuarioSenha = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        Authentication auth = this.authenticationManager.authenticate(usuarioSenha);

        String token = tokenService.generateToken((Conta) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody @Valid RegisterDTO data) {
        try {
            this.authorizationService.registrarUsuario(data);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar: Verifique os dados inseridos (CPF, Nome, etc).");
        }
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

        String nome = (participante != null) ? participante.getNome() : "Usuário";
        String cidade = (participante != null) ? participante.getCidade() : "";
        String resumo = (participante != null) ? participante.getResumo() : "";
        String telefone = (participante != null) ? participante.getTelefone() : "";

        UsuarioResponseDTO response = new UsuarioResponseDTO(
                conta.getId(),
                nome,
                conta.getEmail(),
                telefone,
                cidade,
                resumo,
                conta.getPerfil()
        );

        return ResponseEntity.ok(response);
    }
}