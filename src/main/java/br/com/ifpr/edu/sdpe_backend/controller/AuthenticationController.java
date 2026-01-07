package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.AuthDTO;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.RegisterDTO;
import br.com.ifpr.edu.sdpe_backend.infra.security.TokenService;
import br.com.ifpr.edu.sdpe_backend.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}