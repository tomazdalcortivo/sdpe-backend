package br.com.ifpr.edu.sdpe_backend.service;

import br.com.ifpr.edu.sdpe_backend.domain.Conta;
import br.com.ifpr.edu.sdpe_backend.domain.Coordenador;
import br.com.ifpr.edu.sdpe_backend.domain.DTO.RegisterDTO;
import br.com.ifpr.edu.sdpe_backend.domain.Participante;
import br.com.ifpr.edu.sdpe_backend.domain.enums.TipoPerfil;
import br.com.ifpr.edu.sdpe_backend.repository.ContaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final ContaRepository contaRepository;
    private final ParticipanteService participanteService;
    private final CoordenadorService coordenadorService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return contaRepository.findByEmail(email);
    }

    @Transactional
    public void registrarUsuario(RegisterDTO data) {
        if (this.contaRepository.findByEmail(data.email()) != null) throw new IllegalArgumentException("E-mail já cadastrado.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Conta novaConta = Conta.builder()
                .email(data.email())
                .senha(encryptedPassword)
                .perfil(data.perfil())
                .ativo(true)
                .build();

        this.contaRepository.save(novaConta);

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
    }
}