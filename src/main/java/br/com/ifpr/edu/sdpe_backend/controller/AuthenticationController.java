package br.com.ifpr.edu.sdpe_backend.controller;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.AuthDTO;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.RegisterDTO;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import br.com.ifpr.edu.sdpe_backend.infra.security.TokenService;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import br.com.ifpr.edu.sdpe_backend.service.CoordenadorService;
import br.com.ifpr.edu.sdpe_backend.service.ParticipanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private final ContaRepository repository;

    private final ParticipanteService participanteService;

    private final CoordenadorService coordenadorService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDTO data) {
        UsernamePasswordAuthenticationToken usuarioSenha = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        Authentication auth = this.authenticationManager.authenticate(usuarioSenha);

        String token = tokenService.generateToken((Conta) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Conta novaConta = Conta.builder()
                .email(data.email())
                .senha(encryptedPassword)
                .perfil(data.perfil())
                .ativo(true)
                .build();

        this.repository.save(novaConta);

        if (data.perfil() == TipoPerfil.COORDENADOR) {
            Coordenador coordenador = Coordenador.builder()
                    .nome(data.nome())
                    .dataNascimento(data.dataNascimento())
                    .vinculoInstitucional(data.vinculoInstitucional())
                    .cpf(data.cpf())
                    .cidade(data.cidade())
                    .contato(data.email())
                    .conta(novaConta)
                    .build();

            this.coordenadorService.salvar(coordenador);

        } else if (data.perfil() == TipoPerfil.PARTICIPANTE) {

            Participante participante = Participante.builder()
                    .nome(data.nome())
                    .dataNascimento(data.dataNascimento())
                    .cpf(data.cpf())
                    .cidade(data.cidade())
                    .vinculoInstitucional(data.vinculoInstitucional())
                    .conta(novaConta)
                    .build();

            this.participanteService.salvar(participante);
        }
        return ResponseEntity.ok().build();
    }
}