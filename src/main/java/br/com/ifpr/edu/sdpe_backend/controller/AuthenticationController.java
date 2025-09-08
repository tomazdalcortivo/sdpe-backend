package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.AuthDTO;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.RegisterDTO;
import br.com.ifpr.edu.sdpe_backend.infra.security.TokenService;
import br.com.ifpr.edu.sdpe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;

    private final UserRepository repository;

    private final TokenService tokenService;

    // @Valid para validar email
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDTO data) {
        UsernamePasswordAuthenticationToken usuarioSenha = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        Authentication auth = this.authenticationManager.authenticate(usuarioSenha);

        String token = tokenService.generateToken((Conta) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody RegisterDTO data) {
        if (this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Conta newConta = new Conta(data.login(), encryptedPassword, data.role());

        this.repository.save(newConta);

        return ResponseEntity.ok().build();
    }
}

